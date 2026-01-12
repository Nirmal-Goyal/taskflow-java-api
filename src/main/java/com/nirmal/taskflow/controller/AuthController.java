package com.nirmal.taskflow.controller;

import com.nirmal.taskflow.dto.user.UserRegisterRequest;
import com.nirmal.taskflow.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @Valid
            @RequestBody
            UserRegisterRequest request
    ){
        userService.registerUser(request);
        return new ResponseEntity<>("User registerion request received", HttpStatus.CREATED);
    }
}
