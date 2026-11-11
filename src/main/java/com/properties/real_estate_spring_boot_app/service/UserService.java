package com.properties.real_estate_spring_boot_app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.properties.real_estate_spring_boot_app.model.User;
import com.properties.real_estate_spring_boot_app.repository.UserRepository;
import com.properties.real_estate_spring_boot_app.security.JwtUtil;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder; // needed for secure credentials

    // Find user by email/phone. If not exists → create new user
    public User findOrCreateByContact(String email) {

        Optional<User> existing = userRepo.findByEmail(email);

        if (existing.isPresent()) {
            return existing.get();
        }

        // Create new user
        User user = new User();
        user.setEmail(email);// phone or email
        user.setRole("USER");
        user.setActive(true);

        return userRepo.save(user);
    }

    // Create JWT token for that user
    public String createJwtForUser(User user) {
        return jwtUtil.generateToken(user.getId(), user.getDest(), user.getRole());
    }
}
