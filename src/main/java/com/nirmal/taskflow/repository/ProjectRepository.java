package com.nirmal.taskflow.repository;

import com.nirmal.taskflow.domain.project.Project;
import com.nirmal.taskflow.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    List<Project> findByOwner(User owner);

}
