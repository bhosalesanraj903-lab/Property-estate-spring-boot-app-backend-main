package com.properties.real_estate_spring_boot_app.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.properties.real_estate_spring_boot_app.model.OTP;
import com.properties.real_estate_spring_boot_app.repository.OTPRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OTPService {

    @Autowired
    private OTPRepository otpRepo;

    @Autowired
    private EmailService emailService;

    /**
     * SEND OTP
     */
    public void sendOtp(String email) {

        String otp = String.format("%06d", new Random().nextInt(1000000));

        OTP entity = otpRepo.findByEmail(email)
                .orElseGet(() -> {
                    OTP newOtp = new OTP();
                    newOtp.setEmail(email);
                    return newOtp;
                });

        // 🔁 Update OTP every time
        entity.setOtp(otp);
        entity.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        otpRepo.save(entity); // ✅ insert OR update safely

        emailService.sendOtp(email, otp);
    }

    /**
     * VERIFY OTP
     */
    public boolean verifyOtp(String email, String otp) {

        Optional<OTP> optionalOtp = otpRepo.findByEmail(email);

        if (optionalOtp.isEmpty()) return false;

        OTP savedOtp = optionalOtp.get();

        if (!savedOtp.getOtp().equals(otp)) return false;

        if (savedOtp.getExpiryTime().isBefore(LocalDateTime.now())) {
            otpRepo.deleteByEmail(email);
            return false;
        }

        // Success → remove OTP
        otpRepo.deleteByEmail(email);
        return true;
    }
}
