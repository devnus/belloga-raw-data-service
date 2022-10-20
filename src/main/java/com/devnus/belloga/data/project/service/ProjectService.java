package com.devnus.belloga.data.project.service;

import com.devnus.belloga.data.project.dto.RequestProject;
import com.devnus.belloga.data.project.dto.ResponseProject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {
    boolean saveProject(String enterpriseId, RequestProject.RegisterProject registerProject);
    boolean agreeProject(RequestProject.ApproveProject approveProject);
    Page<ResponseProject.getProject> findProjects(Pageable pageable);
    Page<ResponseProject.getMyProject> findProjectsByEnterpriseId(Pageable pageable, String enterpriseId);
    ResponseProject.getMyProject findProjectByProjectId(String enterpriseId, Long projectId);
    ResponseProject.getUrl getPreSignedUrl(String enterpriseId, Long projectId);
}
