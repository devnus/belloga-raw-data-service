package com.devnus.belloga.data.project.repository;


import com.devnus.belloga.data.project.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findAll(Pageable pageable);
    Page<Project> findByEnterpriseId(Pageable pageable, String enterpriseId);
}

