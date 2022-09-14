package com.devnus.belloga.data.project.service;

import com.devnus.belloga.data.common.dto.ResponseS3;
import com.devnus.belloga.data.common.exception.error.InvalidExtensionException;
import com.devnus.belloga.data.common.exception.error.NotFoundProjectException;
import com.devnus.belloga.data.common.util.S3Finder;
import com.devnus.belloga.data.common.util.S3Uploader;
import com.devnus.belloga.data.project.domain.Project;
import com.devnus.belloga.data.project.dto.RequestProject;
import com.devnus.belloga.data.project.dto.ResponseProject;
import com.devnus.belloga.data.project.repository.ProjectRepository;
import com.devnus.belloga.data.raw.domain.DataType;
import com.devnus.belloga.data.raw.domain.RawData;
import com.devnus.belloga.data.raw.dto.EventRawData;
import com.devnus.belloga.data.raw.event.RawDataProducer;
import com.devnus.belloga.data.raw.repository.RawDataRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final RawDataRepository rawDataRepository;
    private final S3Uploader s3Uploader;
    private final S3Finder s3Finder;
    private final RawDataProducer rawDataProducer;
    private final UserWebClient userWebClient;

    /**
     * project 생성하고, S3에 zip 파일 업로드
     */
    @Override
    @Transactional
    public boolean saveProject(String enterpriseId, RequestProject.RegisterProject registerProject, MultipartFile multipartFile){

        //multipartFile zip 확장자인지 확인
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        if(!extension.equals("zip")){
            throw new InvalidExtensionException("zip 파일이 아닙니다");
        }

        //S3에 zip 파일 업로드
        List<String> uploadResponse = s3Uploader.upload(multipartFile, "org");
        String zipUUID = uploadResponse.get(0);
        String zipUrl = uploadResponse.get(1);

        projectRepository.save(Project.builder()
                .dataType(registerProject.getDataType())
                .enterpriseId(enterpriseId)
                .name(registerProject.getName())
                .zipUUID(zipUUID)
                .zipUrl(zipUrl).build());

        return true;
    }

    /**
     * 프로젝트 승인
     */
    @Override
    @Transactional
    public boolean agreeProject(RequestProject.ApproveProject approveProject){
        Project project = projectRepository.findById(approveProject.getProjectId()).orElseThrow(() -> new NotFoundProjectException());
        List<ResponseS3.S3File> s3Files = s3Finder.findFiles("org", project.getZipUUID());

        //project 버킷의 파일 리스트를 가져와서 DB에 저장 및 전처리 마이크로서비스로 전달
        for(ResponseS3.S3File s3File : s3Files){

            RawData rawData = rawDataRepository.save(RawData.builder()
                    .dataType(DataType.OCR)
                    .enterpriseId(project.getEnterpriseId())
                    .projectId(project.getId())
                    .fileName(s3File.getFileName())
                    .fileUrl(s3File.getFileUrl()).build());

            rawDataProducer.uploadRawData(EventRawData.uploadRawData.builder()
                    .dataType(DataType.OCR)
                    .enterpriseId(project.getEnterpriseId())
                    .projectId(project.getId())
                    .fileName(s3File.getFileName())
                    .fileUrl(s3File.getFileUrl())
                    .rawDataId(rawData.getId()).build());
        }

        //프로젝트 승인
        project.agree();

        return true;
    }

    /**
     * 기업 사용자가 업로드한 프로젝트를 조회
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ResponseProject.getProject> findProjects(Pageable pageable) {
        Page<Project> projects = projectRepository.findAll(pageable);

        Page<ResponseProject.getProject> getProjects = projects.map((Project project) -> {

            //enterpriseId로 동기 통신을 통해 기업 사용자 정보를 가져온다
            String enterpriseName = userWebClient.getEnterpriseInfo(project.getEnterpriseId()).getName();

            return ResponseProject.getProject.builder()
                    .projectId(project.getId())
                    .dataType(project.getDataType())
                    .enterpriseName(enterpriseName)
                    .name(project.getName())
                    .zipUUID(project.getZipUUID())
                    .zipUrl(project.getZipUrl())
                    .isAgreed(project.getIsAgreed()).build();
        });

        return getProjects;
    }
}
