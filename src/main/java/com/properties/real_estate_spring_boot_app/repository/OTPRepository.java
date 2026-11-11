package com.properties.real_estate_spring_boot_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.properties.real_estate_spring_boot_app.model.OTP;

public interface OTPRepository extends JpaRepository<OTP, Long> {

    Optional<OTP> findByEmail(String email);

    @Modifying
    @Query("DELETE FROM OTP o WHERE o.email = :email")
    void deleteByEmail(@Param("email") String email);
}
