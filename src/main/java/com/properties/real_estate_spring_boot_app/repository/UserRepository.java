package com.properties.real_estate_spring_boot_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.properties.real_estate_spring_boot_app.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByDest(String dest);
    Optional<User> findByEmail(String email);
    Optional<User> findByRefreshToken(String refreshToken);
    Optional<User> findByPhone(String phone);

}
