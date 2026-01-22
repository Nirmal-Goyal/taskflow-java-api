package com.nirmal.taskflow.repository;

import com.nirmal.taskflow.domain.project.Project;
import com.nirmal.taskflow.domain.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findByProject(Project project);
}
