package com.nirmal.taskflow.dto.task;

import java.time.Instant;
import java.util.UUID;

public class TaskResponse {

    private UUID id;
    private String title;
    private String description;
    private UUID projectId;
    private Instant createdAt;

    public TaskResponse(UUID id, String title, String description, UUID projectId, Instant createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.projectId = projectId;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
