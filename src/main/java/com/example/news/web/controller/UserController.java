package com.example.news.web.controller;

import com.example.news.aop.UserPermission;
import com.example.news.mapper.UserMapper;
import com.example.news.model.User;
import com.example.news.service.UserService;
import com.example.news.web.model.UserListResponse;
import com.example.news.web.model.UserRequest;
import com.example.news.web.model.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/news-service/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper  userMapper;

    @GetMapping
    public ResponseEntity<UserListResponse> findAll() {
        return ResponseEntity.ok(
                userMapper.userListToResponseList(userService.findAll())
        );
    }

    @GetMapping("/{id}")
    @UserPermission
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                userMapper.userToResponse(userService.findById(id))
        );
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UserRequest request) {
        User user = userService.save(userMapper.requestToUser(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.userToResponse(user));
    }

    @PutMapping("/{id}")
    @UserPermission
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody @Valid UserRequest request) {
        User user = userService.update(userMapper.requestToUser(id, request));
        return ResponseEntity.ok(userMapper.userToResponse(user));
    }

    @DeleteMapping("/{id}")
    @UserPermission
    public ResponseEntity<UserResponse> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
