package com.university.auth.controller;

import com.university.auth.model.User;
import com.university.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        return ResponseEntity.ok(Map.of("message", authService.register(user)));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String token = authService.login(
                credentials.get("username"),
                credentials.get("password")
        );
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestBody Map<String, String> request) {
        boolean valid = authService.validateToken(request.get("token"));
        return ResponseEntity.ok(Map.of("valid", valid));
    }
    @GetMapping("/")
    public String home() {
        return "AuthService is running!";
    }

}