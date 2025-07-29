package com.example.recipesharing.service;

import org.springframework.stereotype.Service;

import com.example.recipesharing.entity.User;
import com.example.recipesharing.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public User findById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    // 他にも必要なら追加できます
}
