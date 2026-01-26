package com.nirmal.taskflow.repository;

import com.nirmal.taskflow.domain.project.Project;
import com.nirmal.taskflow.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    List<Project> findByOwner(User owner);

    @Query("select p from Project p where p.id = :id")
    Optional<Project> findIncludingDeleted(@Param("id") UUID id);

}
