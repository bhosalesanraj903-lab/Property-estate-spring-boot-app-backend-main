package com.properties.real_estate_spring_boot_app.controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.properties.real_estate_spring_boot_app.dto.AuthResponse;
import com.properties.real_estate_spring_boot_app.dto.LoginRequest;
import com.properties.real_estate_spring_boot_app.dto.RefreshTokenRequest;
import com.properties.real_estate_spring_boot_app.model.User;
import com.properties.real_estate_spring_boot_app.repository.UserRepository;
import com.properties.real_estate_spring_boot_app.security.JwtUtil;
import com.properties.real_estate_spring_boot_app.service.AuthService;
import com.properties.real_estate_spring_boot_app.service.OTPService;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private OTPService otpService; // central OTP service

    
    // =========================
    // REFRESH TOKEN
    // =========================
  
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @RequestBody Map<String, String> body
    ) {

        String refreshToken = body.get("refreshToken");

        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Claims claims = jwtUtil.validateAndGetClaims(refreshToken);

        String email = claims.getSubject();
        Long id = claims.get("id", Long.class);
        String role = claims.get("role", String.class);

        String newAccessToken = jwtUtil.generateToken(id, email, role);

        return ResponseEntity.ok(
                new AuthResponse(newAccessToken, refreshToken, role)
        );
    }

    
    // =========================
    // ADMIN LOGIN (EMAIL + PASSWORD)
    // =========================
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

  
    // =========================
    // SEND OTP (EMAIL / PHONE)
    // =========================
   @PostMapping("/send-otp")
public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> body) {

    String email = body.get("email");
    String phone = body.get("phone");

    if ((email == null || email.isBlank()) && (phone == null || phone.isBlank())) {
        return ResponseEntity.badRequest().body("Email or phone required");
    }

//    // determine login identifier
//    String dest = (email != null && !email.isBlank()) ? email : phone;

    User user = userRepo.findByEmail(email).orElseGet(() -> {
        User u = new User();
        u.setEmail(email);
        u.setPhone(phone);
        u.setRole("USER");
        u.setActive(true);
        return userRepo.save(u);
    });

    // 🔁 update missing info if provided
    boolean updated = false;

    if (email != null && user.getEmail() == null) {
        user.setEmail(email);
        updated = true;
    }

    if (phone != null && user.getPhone() == null) {
        user.setPhone(phone);
        updated = true;
    }

    if (updated) {
        userRepo.save(user);
    }

    // 🔥 SEND OTP
    otpService.sendOtp(email);

    return ResponseEntity.ok("OTP sent successfully");
}


    // =========================
    // VERIFY OTP (EMAIL / PHONE)
    // =========================
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> body) {

        String email = body.get("to");
        String otp = body.get("code");

        if (email == null || otp == null) {
            return ResponseEntity.badRequest().body("Email and OTP are required");
        }

        boolean isValid = otpService.verifyOtp(email, otp);

        if (!isValid) {
            return ResponseEntity.badRequest().body("Invalid or expired OTP");
        }

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Mark user verified
        user.setVerified(true);

        // ✅ Generate access token (15 min)
        String accessToken = jwtUtil.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );

        // ✅ Generate refresh token (DB stored)
        String refreshToken = UUID.randomUUID().toString();
        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpiry(
                LocalDateTime.now().plusDays(7)
        );

        userRepo.save(user);

        return ResponseEntity.ok(
                new AuthResponse(
                        accessToken,
                        refreshToken,
                        user.getRole()
                )
        );
    }

}
