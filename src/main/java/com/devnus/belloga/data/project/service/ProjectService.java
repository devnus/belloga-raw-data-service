package com.devnus.belloga.data.project.service;

import com.devnus.belloga.data.project.dto.RequestProject;
import org.springframework.web.multipart.MultipartFile;

public interface ProjectService {
    boolean saveProject(String enterpriseId, RequestProject.RegisterProject registerProject, MultipartFile multipartFile);
    boolean agreeProject(RequestProject.ApproveProject approveProject);
}
