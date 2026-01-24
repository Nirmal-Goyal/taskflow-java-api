package com.nirmal.taskflow.service.impl;

import com.nirmal.taskflow.domain.project.Project;
import com.nirmal.taskflow.domain.task.Task;
import com.nirmal.taskflow.domain.task.TaskStatus;
import com.nirmal.taskflow.domain.user.User;
import com.nirmal.taskflow.dto.task.CreateTaskRequest;
import com.nirmal.taskflow.dto.task.TaskResponse;
import com.nirmal.taskflow.dto.task.UpdateTaskAssigneeRequest;
import com.nirmal.taskflow.dto.task.UpdateTaskStatusRequest;
import com.nirmal.taskflow.exception.BadRequestException;
import com.nirmal.taskflow.exception.UnauthorizedException;
import com.nirmal.taskflow.repository.ProjectRepository;
import com.nirmal.taskflow.repository.TaskRepository;
import com.nirmal.taskflow.repository.UserRepository;
import com.nirmal.taskflow.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public TaskServiceImpl(TaskRepository taskRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TaskResponse createTask(String userId, CreateTaskRequest request, boolean isAdmin){
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if(!isAdmin && !project.getOwner().getId().toString().equals(userId)){
            throw new UnauthorizedException("You do not own this project");
        }

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setProject(project);

        Task saved = taskRepository.save(task);

        return map(saved);

    }

    @Override
    public List<TaskResponse> getTasksByProject(
            UUID projectId,
            String userId,
            boolean isAdmin
    ){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(isAdmin){
            return taskRepository.findByProject(project)
                    .stream()
                    .map(this::map)
                    .toList();
        }

        if(project.getOwner().getId().equals(user.getId())){
            return taskRepository.findByProject(project)
                    .stream()
                    .map(this::map)
                    .toList();
        }

        boolean isMember = project.getMembers()
                .stream()
                .anyMatch(u -> u.getId().equals(user.getId()));

        if(isMember){
            return taskRepository.findByProjectAndAssignee(project, user)
                    .stream()
                    .map(this::map)
                    .toList();
        }

        throw new UnauthorizedException("Access denied");
    }

    @Override
    public TaskResponse updateStatus(
            UUID taskId,
            String userId,
            UpdateTaskStatusRequest request
    ) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Project project = task.getProject();

        if (!project.getOwner().getId().toString().equals(userId)) {
            throw new UnauthorizedException("You are not the project owner");
        }

        validateTransition(task.getStatus(), request.getStatus());

        task.setStatus(request.getStatus());
        Task updated = taskRepository.save(task);

        return map(updated);
    }


    private void validateTransition(TaskStatus current, TaskStatus next) {

        if(current == TaskStatus.COMPLETED){
            throw new BadRequestException("Task is already completed");
        }

        if(current == TaskStatus.TODO && next == TaskStatus.COMPLETED){
            throw new BadRequestException("Task must be IN_PROGRESS before COMPLETED");
        }
    }

    @Override
    public TaskResponse updateTaskAssignee(
            UUID taskId,
            String userId,
            UpdateTaskAssigneeRequest request
    ){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not Found"));

        Project project = task.getProject();

        if(!project.getOwner().getId().toString().equals(userId)){
            throw new UnauthorizedException("Only project owner can reassign tasks");
        }

        User assignee = project.getMembers()
                .stream()
                .filter(u -> u.getId().equals(request.getAssigneeId()))
                .findFirst()
                .orElseThrow(
                        () -> new BadRequestException("User is not a member of this project")
                );

        task.setAssignee(assignee);

        Task updated = taskRepository.save(task);

        return map(updated);
    }

    @Override
    public List<TaskResponse> getMyTasks(String userId) {
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        return taskRepository.findByAssignee(user)
                .stream()
                .map(this::map)
                .toList();
    }

    private TaskResponse map(Task task){
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getProject().getId(),
                task.getCreatedAt(),
                task.getStatus()
        );
    }
}
