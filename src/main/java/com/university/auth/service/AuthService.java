package com.university.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.university.auth.model.User;
import com.university.auth.repository.UserRepository;
import com.university.auth.security.JwtUtil;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    public String register(User user) {
        // Vérifier si l'utilisateur existe déjà
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        // Encoder le mot de passe
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Définir un rôle par défaut si non spécifié
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("STUDENT");
        }
        
        // Sauvegarder l'utilisateur
        userRepository.save(user);
        
        return "User registered successfully";
    }
    
    public String login(String username, String password) {
        // Trouver l'utilisateur
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Vérifier le mot de passe
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        
        // Générer et retourner le token
        return jwtUtil.generateToken(user.getUsername(), user.getRole());
    }
    
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }
}