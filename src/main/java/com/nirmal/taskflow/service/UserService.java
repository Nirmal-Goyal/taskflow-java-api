package com.nirmal.taskflow.service;

import com.nirmal.taskflow.dto.user.UserRegisterRequest;

public interface UserService {
    void registerUser(UserRegisterRequest request);
}
