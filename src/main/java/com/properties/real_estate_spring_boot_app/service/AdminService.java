package com.properties.real_estate_spring_boot_app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.properties.real_estate_spring_boot_app.dto.AdminCreateRequest;
import com.properties.real_estate_spring_boot_app.dto.AdminLoginRequest;
import com.properties.real_estate_spring_boot_app.dto.AdminResponse;
import com.properties.real_estate_spring_boot_app.dto.AdminUpdateRequest;
import com.properties.real_estate_spring_boot_app.dto.AuthResponse;
import com.properties.real_estate_spring_boot_app.model.Admin;
import com.properties.real_estate_spring_boot_app.model.User;
import com.properties.real_estate_spring_boot_app.repository.AdminRepository;
import com.properties.real_estate_spring_boot_app.repository.UserRepository;
import com.properties.real_estate_spring_boot_app.security.JwtUtil;

@Service
public class AdminService {
	
    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Cloudinary cloudinary;
    
    // Create Admin
    
    public AdminResponse createAdmin(AdminCreateRequest request) {

        // ❌ Duplicate check
        if (adminRepo.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Admin with this email already exists");
        }

        Admin admin = new Admin();
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setVerified(true); // or false if OTP based

        Admin savedAdmin = adminRepo.save(admin);

        return new AdminResponse(
                savedAdmin.getId(),
                savedAdmin.getEmail(),
                savedAdmin.isVerified()
        );
    }
    
 // 🔐 LOGIN
    public AuthResponse login(AdminLoginRequest request) {

        Admin admin = adminRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        if (!admin.isVerified()) {
            throw new RuntimeException("Admin not verified");
        }

        String token = jwtUtil.generateToken(
                admin.getId(),
                admin.getEmail(),
                "ADMIN"
        );
        String refreshToken = jwtUtil.generateRefreshToken(
                admin.getId(),
                admin.getEmail()
        );

        return new AuthResponse(token,token,"ADMIN");
    }

    // ✏️ UPDATE EMAIL / PASSWORD
    public void updateAdmin(Long id, AdminUpdateRequest req) {

        Admin admin = adminRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (req.getEmail() != null) {
            admin.setEmail(req.getEmail());
        }

        if (req.getPassword() != null) {
            admin.setPassword(passwordEncoder.encode(req.getPassword()));
        }

        adminRepo.save(admin);
    }
    

    /* ---------- USERS ---------- */

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    /* ---------- IMAGE UPLOAD ---------- */

    public List<String> uploadImages(MultipartFile[] files) {

    List<String> imageUrls = new ArrayList<>();

    try {
        for (MultipartFile file : files) {

            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                        "folder", "properties"
                    )
            );

            imageUrls.add(uploadResult.get("secure_url").toString());
        }
        return imageUrls;

    } catch (Exception e) {
        throw new RuntimeException("Image upload failed", e);
    }
}

}
