package com.nirmal.taskflow.repository;

import com.nirmal.taskflow.domain.project.Project;
import com.nirmal.taskflow.domain.task.Task;
import com.nirmal.taskflow.domain.task.TaskStatus;
import com.nirmal.taskflow.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findByProject(Project project);

    List<Task> findByProjectAndAssignee(Project project, User assignee);

    Page<Task> findByAssignee(User assignee, Pageable pageable);

    Page<Task> findByAssigneeAndStatus(
            User assignee,
            TaskStatus status,
            Pageable pageable
    );

    @Modifying
    @Query("update Task t set t.deleted = true where t.project = :project")
    void softDeleteByProject(@Param("project") Project project);
}
