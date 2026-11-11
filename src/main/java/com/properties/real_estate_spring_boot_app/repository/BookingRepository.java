package com.properties.real_estate_spring_boot_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.properties.real_estate_spring_boot_app.model.Booking;

import jakarta.validation.constraints.Email;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // 🔹 user-wise bookings
    List<Booking> findByUserId(Long userId);
    List<Booking> findByEmail(String email);
    void deleteById(Long id);
    List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);
}
