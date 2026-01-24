package com.nirmal.taskflow.service;

import com.nirmal.taskflow.dto.project.CreateProjectRequest;
import com.nirmal.taskflow.dto.project.ProjectResponse;

import java.util.List;
import java.util.UUID;

public interface ProjectService {

    ProjectResponse createProject(String userId, CreateProjectRequest request);

    List<ProjectResponse> getMyProjects(String userId);

    ProjectResponse getProjectById(UUID projectId, String userId, boolean isAdmin);

    void addMember(UUID projectId, UUID memberId, String name);
}
