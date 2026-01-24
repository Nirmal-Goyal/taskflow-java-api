package com.nirmal.taskflow.controller;

import com.nirmal.taskflow.dto.task.CreateTaskRequest;
import com.nirmal.taskflow.dto.task.TaskResponse;
import com.nirmal.taskflow.dto.task.UpdateTaskAssigneeRequest;
import com.nirmal.taskflow.dto.task.UpdateTaskStatusRequest;
import com.nirmal.taskflow.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    private Authentication auth(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private boolean isAdmin(){
        return auth().getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @PostMapping
    public TaskResponse create(
            @Valid
            @RequestBody CreateTaskRequest request
            ){
        return taskService.createTask(auth().getName(), request, isAdmin());
    }

    @GetMapping("/project/{projectId}")
    public List<TaskResponse> getByProject(
            @PathVariable UUID projectId
            ){
        return taskService.getTasksByProject(projectId, auth().getName(), isAdmin());
    }

    @PatchMapping("/{taskId}/status")
    public TaskResponse updateStatus(
            @PathVariable UUID taskId,
            @Valid @RequestBody UpdateTaskStatusRequest request
    ){
        return taskService.updateStatus(
                        taskId,
                        auth().getName(),
                        request
        );
    }

    @PatchMapping("/{taskId}/assignee")
    public TaskResponse updateAssignee(
            @PathVariable UUID taskId,
            @Valid @RequestBody UpdateTaskAssigneeRequest request
            ){
        return taskService.updateTaskAssignee(
                taskId,
                auth().getName(),
                request
        );
    }

    @GetMapping("/me")
    public List<TaskResponse> myTasks(){
        return taskService.getMyTasks(auth().getName());
    }
}
