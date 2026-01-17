package com.nirmal.taskflow.controller;

import com.nirmal.taskflow.domain.user.User;
import com.nirmal.taskflow.dto.auth.AuthResponse;
import com.nirmal.taskflow.dto.user.UserLoginRequest;
import com.nirmal.taskflow.dto.user.UserRegisterRequest;
import com.nirmal.taskflow.dto.user.UserResponse;
import com.nirmal.taskflow.security.JwtService;
import com.nirmal.taskflow.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService){
        this.userService = userService;
        this.jwtService = jwtService;
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
    public ResponseEntity<AuthResponse> login(
            @RequestBody
            UserLoginRequest request
    ){
        User user = userService.login(request);
        String token = jwtService.generateToken(user.getId().toString());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
