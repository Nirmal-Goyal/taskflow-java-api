package com.nirmal.taskflow.dto.task;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class UpdateTaskAssigneeRequest {

    @NotNull
    private UUID assigneeId;

    public UUID getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(UUID assigneeId) {
        this.assigneeId = assigneeId;
    }
}
