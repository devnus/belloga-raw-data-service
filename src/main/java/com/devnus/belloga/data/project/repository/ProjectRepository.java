package com.devnus.belloga.data.project.repository;


import com.devnus.belloga.data.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}

