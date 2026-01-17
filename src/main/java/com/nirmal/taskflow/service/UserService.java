package com.nirmal.taskflow.service;

import com.nirmal.taskflow.domain.user.User;
import com.nirmal.taskflow.dto.user.UserLoginRequest;
import com.nirmal.taskflow.dto.user.UserRegisterRequest;
import com.nirmal.taskflow.dto.user.UserResponse;

public interface UserService {
    UserResponse register(UserRegisterRequest request);
    User login(UserLoginRequest request);
}
