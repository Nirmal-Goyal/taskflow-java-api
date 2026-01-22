package com.nirmal.taskflow.service.impl;

import com.nirmal.taskflow.domain.user.User;
import com.nirmal.taskflow.domain.user.UserRole;
import com.nirmal.taskflow.dto.user.UpdateUserRoleRequest;
import com.nirmal.taskflow.dto.user.UserLoginRequest;
import com.nirmal.taskflow.dto.user.UserRegisterRequest;
import com.nirmal.taskflow.dto.user.UserResponse;
import com.nirmal.taskflow.exception.BadRequestException;
import com.nirmal.taskflow.exception.UnauthorizedException;
import com.nirmal.taskflow.repository.UserRepository;
import com.nirmal.taskflow.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse register(UserRegisterRequest request){

        if(userRepository.existsByEmail(request.getEmail())){
            throw new BadRequestException("Email already registered");
        }

        User user  = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        user.setRole(UserRole.USER);

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getCreatedAt()
        );
    }

    @Override
    public User login(UserLoginRequest request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or pssword"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new UnauthorizedException("Invalid password");
        }

        return user;
    }

    @Override
    public UserResponse getCurrentUser(String userId){
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole(),
                        user.getCreatedAt()
                ))
                .toList();
    }

    @Override
    public UserResponse updateUserRole(UUID userId, UpdateUserRoleRequest request){

        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();

        if(userId.toString().equals(currentUserId)){
            throw new BadRequestException("You cannot update yourself as you are already logged in");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(request.getRole());

        User updatedUser = userRepository.save(user);

        return new UserResponse(
                updatedUser.getId(),
                updatedUser.getName(),
                updatedUser.getEmail(),
                updatedUser.getRole(),
                updatedUser.getCreatedAt()
        );
    }

}
