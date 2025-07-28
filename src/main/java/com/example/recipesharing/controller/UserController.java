package com.example.recipesharing.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.recipesharing.entity.User;
import com.example.recipesharing.repository.UserRepository;

@Controller
public class UserController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // --- 新規登録 ---
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        if (userRepo.findByUsername(user.getUsername()) != null) {
            model.addAttribute("error", "すでに登録されています");
            return "register";
        }

        // パスワードを暗号化してから保存
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 権限を設定
        user.setRole("USER");

        // 保存は1回だけ
        userRepo.save(user);

        return "redirect:/login";
    }
}