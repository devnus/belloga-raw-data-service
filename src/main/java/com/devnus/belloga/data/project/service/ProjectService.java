package com.devnus.belloga.data.project.service;

import com.devnus.belloga.data.project.dto.RequestProject;
import com.devnus.belloga.data.project.dto.ResponseProject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ProjectService {
    boolean saveProject(String enterpriseId, RequestProject.RegisterProject registerProject, MultipartFile multipartFile);
    boolean agreeProject(RequestProject.ApproveProject approveProject);
    Page<ResponseProject.getProject> findProjects(Pageable pageable);
}
