package com.nirmal.taskflow.service.impl;

import com.nirmal.taskflow.domain.project.Project;
import com.nirmal.taskflow.domain.user.User;
import com.nirmal.taskflow.dto.project.CreateProjectRequest;
import com.nirmal.taskflow.dto.project.ProjectResponse;
import com.nirmal.taskflow.exception.BadRequestException;
import com.nirmal.taskflow.exception.UnauthorizedException;
import com.nirmal.taskflow.repository.ProjectRepository;
import com.nirmal.taskflow.repository.UserRepository;
import com.nirmal.taskflow.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ProjectResponse createProject(String userId, CreateProjectRequest request){
        User owner = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = new Project();
        project.setName(request.getName());
        project.setOwner(owner);
        project.getMembers().add(owner);

        Project saved = projectRepository.save(project);

        return map(saved);
    }

    @Override
    public List<ProjectResponse> getMyProjects(String userId){
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        return projectRepository.findByOwner(user)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public ProjectResponse getProjectById(UUID projectId, String userId, boolean isAdmin){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if(!isAdmin && !project.getOwner().getId().toString().equals(userId)){
            throw new UnauthorizedException("Access denied");
        }

        return map(project);
    }

    @Override
    public void addMember(UUID projectId, UUID memberId, String userId){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if(!project.getOwner().getId().toString().equals(userId)){
            throw new UnauthorizedException("Only owner can add members");
        }

        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(project.getMembers().contains(user)){
            throw new BadRequestException("User already a member of this project");
        }

        project.getMembers().add(user);
        projectRepository.save(project);
    }

    private ProjectResponse map(Project p){
        return new ProjectResponse(
                p.getId(),
                p.getName(),
                p.getOwner().getId(),
                p.getCreatedAt()
        );
    }

}
