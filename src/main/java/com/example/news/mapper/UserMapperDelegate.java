package com.example.news.mapper;

import com.example.news.config.SecurityConfig;
import com.example.news.model.Role;
import com.example.news.model.User;
import com.example.news.service.UserService;
import com.example.news.web.model.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

public abstract class UserMapperDelegate implements UserMapper {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User requestToUser(UserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(Role.valueOf(request.getRole())));

        return user;
    }

    @Override
    public User requestToUser(Long id, UserRequest request) {
        User user = new User();
        user.setId(id);
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return user;
    }
}
