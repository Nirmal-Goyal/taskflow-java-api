package com.nirmal.taskflow.service;

import com.nirmal.taskflow.dto.task.CreateTaskRequest;
import com.nirmal.taskflow.dto.task.TaskResponse;

import java.util.List;
import java.util.UUID;

public interface TaskService {

    TaskResponse createTask(String userId, CreateTaskRequest request, boolean isAdmin);

    List<TaskResponse> getTasksByProject(UUID projectId, String userId, boolean isAdmin);
}
