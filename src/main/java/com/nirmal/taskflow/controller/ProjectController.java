package com.nirmal.taskflow.controller;

import com.nirmal.taskflow.dto.project.CreateProjectRequest;
import com.nirmal.taskflow.dto.project.ProjectResponse;
import com.nirmal.taskflow.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    private Authentication auth(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @PostMapping
    public ProjectResponse create(
            @Valid
            @RequestBody CreateProjectRequest request
            ){
        return projectService.createProject(auth().getName(), request);
    }

    @GetMapping("/me")
    public List<ProjectResponse> myProjects() {
        return projectService.getMyProjects(auth().getName());
    }

    @GetMapping("/{id}")
    public ProjectResponse getById(
            @PathVariable UUID id
    ){
        boolean isAdmin = auth().getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        return projectService.getProjectById(id, auth().getName(), isAdmin);
    }
}
