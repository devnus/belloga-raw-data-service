package com.devnus.belloga.data.project.controller;

import com.devnus.belloga.data.common.aop.annotation.GetAccountIdentification;
import com.devnus.belloga.data.common.aop.annotation.UserRole;
import com.devnus.belloga.data.common.dto.CommonResponse;
import com.devnus.belloga.data.project.dto.RequestProject;
import com.devnus.belloga.data.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * project 컨트롤러
 */
@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping(value="/v1/project", consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponse> registerProject(@GetAccountIdentification(role = UserRole.ENTERPRISE) String enterpriseId, @RequestPart(value="project") @Valid RequestProject.RegisterProject registerProject, @RequestPart(value="upload", required = true) MultipartFile multipartFile) {

        CommonResponse response = CommonResponse.builder()
                .success(true)
                .response(projectService.saveProject(enterpriseId, registerProject, multipartFile))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
