package com.nirmal.taskflow.controller;

import com.nirmal.taskflow.dto.user.UserLoginRequest;
import com.nirmal.taskflow.dto.user.UserRegisterRequest;
import com.nirmal.taskflow.dto.user.UserResponse;
import com.nirmal.taskflow.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(
            @Valid
            @RequestBody
            UserRegisterRequest request
    ){
        return userService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(
            @Valid
            @RequestBody
            UserLoginRequest request
    ){
        return ResponseEntity.ok(userService.login(request));
    }
}
