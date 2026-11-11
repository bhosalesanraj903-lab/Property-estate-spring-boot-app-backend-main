package com.properties.real_estate_spring_boot_app.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.properties.real_estate_spring_boot_app.dto.AuthResponse;
import com.properties.real_estate_spring_boot_app.dto.LoginRequest;
import com.properties.real_estate_spring_boot_app.dto.RefreshTokenRequest;
import com.properties.real_estate_spring_boot_app.model.User;
import com.properties.real_estate_spring_boot_app.repository.UserRepository;
import com.properties.real_estate_spring_boot_app.security.JwtUtil;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repo;
    private final JwtUtil jwt;

    @Autowired
    public AuthService(
            PasswordEncoder passwordEncoder,
            UserRepository repo,
            JwtUtil jwt
    ) {
        this.passwordEncoder = passwordEncoder;
        this.repo = repo;
        this.jwt = jwt;
    }

    /* ---------- LOGIN ---------- */
    public AuthResponse login(LoginRequest req) {

        User user = repo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwt.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
        String refreshToken = UUID.randomUUID().toString();

        user.setRefreshToken(refreshToken);
        repo.save(user);

        return new AuthResponse(
                accessToken,
                refreshToken,
                user.getRole()
        );
    }

    /* ---------- REFRESH TOKEN ---------- */
    public AuthResponse refreshToken(RefreshTokenRequest req) {

        User user = repo.findByRefreshToken(req.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        String newAccessToken = jwt.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );

        return new AuthResponse(
                newAccessToken,
                req.getRefreshToken(),
                user.getRole()
        );
    }
}
