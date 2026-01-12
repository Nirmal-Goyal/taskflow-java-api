package com.nirmal.taskflow.service.impl;

import com.nirmal.taskflow.domain.user.User;
import com.nirmal.taskflow.domain.user.UserRepository;
import com.nirmal.taskflow.dto.user.UserRegisterRequest;
import com.nirmal.taskflow.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public void registerUser(UserRegisterRequest request){
        User user  = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        userRepository.save(user);
    }

}
