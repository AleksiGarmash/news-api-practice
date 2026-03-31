package com.example.news.service;

import com.example.news.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(Long id);
    User save(User user);
    User update(User user);
    void deleteById(Long id);
    User findByName(String username);
}
