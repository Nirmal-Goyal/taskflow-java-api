package com.nirmal.taskflow.service.impl;

import com.nirmal.taskflow.domain.project.Project;
import com.nirmal.taskflow.domain.task.Task;
import com.nirmal.taskflow.dto.task.CreateTaskRequest;
import com.nirmal.taskflow.dto.task.TaskResponse;
import com.nirmal.taskflow.exception.UnauthorizedException;
import com.nirmal.taskflow.repository.ProjectRepository;
import com.nirmal.taskflow.repository.TaskRepository;
import com.nirmal.taskflow.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public TaskServiceImpl(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
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
    public List<TaskResponse> getTasksByProject(UUID projectId, String userId, boolean isAdmin){

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if(!isAdmin && !project.getOwner().getId().toString().equals(userId)){
            throw new UnauthorizedException("Access denied");
        }

        return taskRepository.findByProject(project)
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
                task.getCreatedAt()
        );
    }
}
