package com.properties.real_estate_spring_boot_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.properties.real_estate_spring_boot_app.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByEmail(String email);
    boolean existsByEmail(String email);
}
