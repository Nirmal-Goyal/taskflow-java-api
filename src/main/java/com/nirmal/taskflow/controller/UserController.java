package com.nirmal.taskflow.controller;

import com.nirmal.taskflow.dto.user.UpdateUserRoleRequest;
import com.nirmal.taskflow.dto.user.UserResponse;
import com.nirmal.taskflow.repository.UserRepository;
import com.nirmal.taskflow.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService){
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/me")
    public UserResponse getMe(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId = authentication.getName();

        return userService.getCurrentUser(userId);
    }

    @PatchMapping("/{id}/role")
    public UserResponse updateUserRole(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRoleRequest request
            ){
        return userService.updateUserRole(id, request);
    }
}
