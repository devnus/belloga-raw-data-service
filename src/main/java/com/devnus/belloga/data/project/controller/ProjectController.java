package com.devnus.belloga.data.project.controller;

import com.devnus.belloga.data.common.aop.annotation.GetAccountIdentification;
import com.devnus.belloga.data.common.aop.annotation.UserRole;
import com.devnus.belloga.data.common.dto.CommonResponse;
import com.devnus.belloga.data.project.dto.RequestProject;
import com.devnus.belloga.data.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    /**
     * 프로젝트 생성
     */
    @PostMapping(value="/v1/project", consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponse> registerProject(@GetAccountIdentification(role = UserRole.ENTERPRISE) String enterpriseId, @RequestPart(value="project") @Valid RequestProject.RegisterProject registerProject, @RequestPart(value="upload", required = true) MultipartFile multipartFile) {

        CommonResponse response = CommonResponse.builder()
                .success(true)
                .response(projectService.saveProject(enterpriseId, registerProject, multipartFile))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 프로젝트 승인
     */
    @PostMapping("/v1/project/approve")
    public ResponseEntity<CommonResponse> approveProject(@GetAccountIdentification(role = UserRole.ADMIN) String adminId, @RequestBody @Valid RequestProject.ApproveProject approveProject) {

        CommonResponse response = CommonResponse.builder()
                .success(true)
                .response(projectService.agreeProject(approveProject))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 관리자가 기업이 생성한 프로젝트 리스트 조회
     */
    @GetMapping("/v1/project")
    public ResponseEntity<CommonResponse> getProject(@GetAccountIdentification(role = UserRole.ADMIN) String adminId, Pageable pageable){

        CommonResponse response = CommonResponse.builder()
                .success(true)
                .response(projectService.findProjects(pageable))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 기업 사용자가 자신이 생성한 프로젝트 리스트 조회
     */
    @GetMapping("/v1/project/my")
    public ResponseEntity<CommonResponse> getMyProject(@GetAccountIdentification(role = UserRole.ENTERPRISE) String enterpriseId, Pageable pageable){

        CommonResponse response = CommonResponse.builder()
                .success(true)
                .response(projectService.findProjectsByEnterpriseId(pageable, enterpriseId))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 기업 사용자가 자신의 프로젝트를 projectId로 조회
     */
    @GetMapping("/v1/project/my/{projectId}")
    public ResponseEntity<CommonResponse> getMyProjectByProjectId(@GetAccountIdentification(role = UserRole.ENTERPRISE) String enterpriseId, @PathVariable Long projectId){

        CommonResponse response = CommonResponse.builder()
                .success(true)
                .response(projectService.findProjectByProjectId(enterpriseId, projectId))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
