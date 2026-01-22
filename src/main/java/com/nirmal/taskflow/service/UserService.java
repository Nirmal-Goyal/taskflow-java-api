package com.nirmal.taskflow.service;

import com.nirmal.taskflow.domain.user.User;
import com.nirmal.taskflow.dto.user.UpdateUserRoleRequest;
import com.nirmal.taskflow.dto.user.UserLoginRequest;
import com.nirmal.taskflow.dto.user.UserRegisterRequest;
import com.nirmal.taskflow.dto.user.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponse register(UserRegisterRequest request);
    User login(UserLoginRequest request);

    UserResponse getCurrentUser(String userId);

    UserResponse updateUserRole(UUID userId, UpdateUserRoleRequest request);

    List<UserResponse> getAllUsers();
}
