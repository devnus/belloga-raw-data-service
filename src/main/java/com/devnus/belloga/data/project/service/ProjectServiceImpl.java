package com.devnus.belloga.data.project.service;

import com.devnus.belloga.data.common.dto.ResponseS3;
import com.devnus.belloga.data.common.exception.error.InvalidAccountIdException;
import com.devnus.belloga.data.common.exception.error.NotFoundProjectException;
import com.devnus.belloga.data.common.util.S3Finder;
import com.devnus.belloga.data.common.util.S3Uploader;
import com.devnus.belloga.data.project.domain.Project;
import com.devnus.belloga.data.project.dto.RequestProject;
import com.devnus.belloga.data.project.dto.ResponseLabeling;
import com.devnus.belloga.data.project.dto.ResponseProject;
import com.devnus.belloga.data.project.repository.ProjectRepository;
import com.devnus.belloga.data.raw.domain.DataType;
import com.devnus.belloga.data.raw.domain.RawData;
import com.devnus.belloga.data.raw.dto.EventRawData;
import com.devnus.belloga.data.raw.event.RawDataProducer;
import com.devnus.belloga.data.raw.repository.RawDataRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final RawDataRepository rawDataRepository;
    private final S3Uploader s3Uploader;
    private final S3Finder s3Finder;
    private final RawDataProducer rawDataProducer;
    private final UserWebClient userWebClient;
    private final LabelingWebClient labelingWebClient;

    /**
     * S3에 생성할 프로젝트의 파일명 생성후 프로젝트 생성
     */
    @Override
    @Transactional
    public ResponseProject.registerProject saveProject(String enterpriseId, RequestProject.RegisterProject registerProject){

        //해당 프로젝트에 업로드될 파일명 생성
        String zipUUID = UUID.randomUUID().toString() + ".zip";

        Project project = projectRepository.save(Project.builder()
                .dataType(registerProject.getDataType())
                .enterpriseId(enterpriseId)
                .name(registerProject.getName())
                .zipUUID(zipUUID)
                .zipUrl(zipUUID)
                .description(registerProject.getDescription()).build());

        return ResponseProject.registerProject.builder().projectId(project.getId()).build();
    }

    /**
     * 해당 프로젝트의 Pre Signed URL 생성
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseProject.getUrl getPreSignedUrl(String enterpriseId, Long projectId){

        Project project = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundProjectException());

        if(!project.getEnterpriseId().equals(enterpriseId)){
            throw new InvalidAccountIdException();
        }

        String preSignedUrl = s3Uploader.getPreSignedUrl("org",project.getZipUUID());

        return ResponseProject.getUrl.builder().Url(preSignedUrl).build();
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
     * 기업 사용자가 업로드한 모든 프로젝트를 조회
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
                    .description(project.getDescription())
                    .createDate(project.getCreatedDate())
                    .isAgreed(project.getIsAgreed()).build();
        });

        return getProjects;
    }

    /**
     * 기업 사용자 ID를 이용해 해당 기업 사용자가 업로드한 프로젝트 조회
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ResponseProject.getMyProject> findProjectsByEnterpriseId(Pageable pageable, String enterpriseId){
        Page<Project> projects = projectRepository.findByEnterpriseId(pageable, enterpriseId);

        Page<ResponseProject.getMyProject> getProjects = projects.map((Project project) -> {

            //프로젝트의 dataType과 projectId로 동기 통신을 통해 프로젝트의 라벨링 진행도를 가져온다
            ResponseLabeling.ProgressRate progressRate = labelingWebClient.getProgressRate(project.getDataType(), project.getId());

            return ResponseProject.getMyProject.builder()
                    .projectId(project.getId())
                    .dataType(project.getDataType())
                    .name(project.getName())
                    .zipUUID(project.getZipUUID())
                    .zipUrl(project.getZipUrl())
                    .description(project.getDescription())
                    .createdDate(project.getCreatedDate())
                    .progressRate(progressRate.getProgressRate())
                    .isAgreed(project.getIsAgreed()).build();
        });

        return getProjects;
    }

    /**
     * projectId를 통해 프로젝트 조회
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseProject.getMyProject findProjectByProjectId(String enterpriseId, Long projectId){
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundProjectException());

        //프로젝트에 대한 권한이 없을때
        if(!project.getEnterpriseId().equals(enterpriseId)){
            throw new InvalidAccountIdException();
        }

        //프로젝트의 dataType과 projectId로 동기 통신을 통해 프로젝트의 라벨링 진행도를 가져온다
        ResponseLabeling.ProgressRate progressRate = labelingWebClient.getProgressRate(project.getDataType(), project.getId());

        return ResponseProject.getMyProject.builder()
                .projectId(project.getId())
                .dataType(project.getDataType())
                .name(project.getName())
                .zipUUID(project.getZipUUID())
                .zipUrl(project.getZipUrl())
                .description(project.getDescription())
                .createdDate(project.getCreatedDate())
                .progressRate(progressRate.getProgressRate())
                .isAgreed(project.getIsAgreed()).build();
    }
}
