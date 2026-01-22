package com.nirmal.taskflow.dto.user;

import com.nirmal.taskflow.domain.user.UserRole;
import jakarta.validation.constraints.NotNull;

public class UpdateUserRoleRequest {

    @NotNull
    private UserRole role;

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
