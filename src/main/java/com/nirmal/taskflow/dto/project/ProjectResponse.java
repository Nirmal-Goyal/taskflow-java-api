package com.nirmal.taskflow.dto.project;

import java.time.Instant;
import java.util.UUID;

public class ProjectResponse {

    private UUID id;
    private String name;
    private UUID ownerId;
    private Instant createdAt;

    public ProjectResponse(UUID id, String name, UUID ownerId, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
