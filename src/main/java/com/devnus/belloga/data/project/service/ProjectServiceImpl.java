package com.devnus.belloga.data.project.service;

import com.devnus.belloga.data.common.exception.error.InvalidExtensionException;
import com.devnus.belloga.data.common.util.S3Uploader;
import com.devnus.belloga.data.project.domain.Project;
import com.devnus.belloga.data.project.dto.RequestProject;
import com.devnus.belloga.data.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final S3Uploader s3Uploader;
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
        String zipUrl = s3Uploader.upload(multipartFile, "dev-data");

        projectRepository.save(Project.builder()
                .dataType(registerProject.getDataType())
                .enterpriseId(enterpriseId)
                .name(registerProject.getName())
                .zipUrl(zipUrl).build());

        return true;
    }
}
