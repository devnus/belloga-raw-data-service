package com.devnus.belloga.data.project.controller;

import com.devnus.belloga.data.common.aop.annotation.GetAccountIdentification;
import com.devnus.belloga.data.common.aop.annotation.UserRole;
import com.devnus.belloga.data.common.dto.CommonResponse;
import com.devnus.belloga.data.project.dto.RequestProject;
import com.devnus.belloga.data.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * project 컨트롤러
 */
@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    private final ProjectService projectService;

    /**
     * 프로젝트 생성
     */
    @PostMapping(value="/v1/project")
    public ResponseEntity<CommonResponse> registerProject(@GetAccountIdentification(role = UserRole.ENTERPRISE) String enterpriseId,  @RequestBody @Valid RequestProject.RegisterProject registerProject) {

        CommonResponse response = CommonResponse.builder()
                .success(true)
                .response(projectService.saveProject(enterpriseId, registerProject))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 해당 프로젝트의 Pre-SignedURL 요청
     */
    @GetMapping("/v1/project/{projectId}/url")
    public ResponseEntity<CommonResponse> requestPreSignedURL(@GetAccountIdentification(role = UserRole.ENTERPRISE) String enterpriseId, @PathVariable Long projectId){

        CommonResponse response = CommonResponse.builder()
                .success(true)
                .response(projectService.getPreSignedUrl(enterpriseId, projectId))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * 프로젝트 승인
     */
    @PatchMapping("/v1/project/approve")
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
    @GetMapping("/v1/user/project")
    public ResponseEntity<CommonResponse> getUserProject(@GetAccountIdentification(role = UserRole.ENTERPRISE) String enterpriseId, Pageable pageable){

        CommonResponse response = CommonResponse.builder()
                .success(true)
                .response(projectService.findProjectsByEnterpriseId(pageable, enterpriseId))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 기업 사용자가 자신의 프로젝트를 projectId로 조회
     */
    @GetMapping("/v1/user/project/{projectId}")
    public ResponseEntity<CommonResponse> getUserProjectByProjectId(@GetAccountIdentification(role = UserRole.ENTERPRISE) String enterpriseId, @PathVariable Long projectId){

        CommonResponse response = CommonResponse.builder()
                .success(true)
                .response(projectService.findProjectByProjectId(enterpriseId, projectId))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
