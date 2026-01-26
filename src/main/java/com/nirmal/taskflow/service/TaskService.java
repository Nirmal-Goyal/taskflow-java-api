package com.nirmal.taskflow.service;

import com.nirmal.taskflow.domain.task.TaskStatus;
import com.nirmal.taskflow.dto.task.CreateTaskRequest;
import com.nirmal.taskflow.dto.task.TaskResponse;
import com.nirmal.taskflow.dto.task.UpdateTaskAssigneeRequest;
import com.nirmal.taskflow.dto.task.UpdateTaskStatusRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface TaskService {

    TaskResponse createTask(String userId, CreateTaskRequest request, boolean isAdmin);

    List<TaskResponse> getTasksByProject(UUID projectId, String userId, boolean isAdmin);

    TaskResponse updateStatus(
            UUID taskId,
            String userId,
            UpdateTaskStatusRequest request,
            boolean isAdmin
    );

    TaskResponse updateTaskAssignee(
            UUID taskId,
            String userId,
            UpdateTaskAssigneeRequest request
    );

    Page<TaskResponse> getMyTasks(
            String userId,
            TaskStatus status,
            int page,
            int size
    );

    void deleteTask(UUID taskId, String userId);
}
