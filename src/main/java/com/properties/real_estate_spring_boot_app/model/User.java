package com.properties.real_estate_spring_boot_app.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "dest")
    }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;
    private String phone;

    /**
     * Primary login identifier
     * email OR phone
     */
    @Column(nullable = false, unique = true)
    private String dest;

    private String password;

    @Column(nullable = false)
    private String role = "USER";

    private boolean active = true;
    private boolean verified = false;

    private String refreshToken;
    private LocalDateTime refreshTokenExpiry;

    // ---------------- setters ----------------

    public void setEmail(String email) {
        this.email = email;
        if (email != null && this.dest == null) {
            this.dest = email;
        }
    }

    public void setPhone(String phone) {
        this.phone = phone;
        if (phone != null && this.dest == null) {
            this.dest = phone;
        }
    }

    // ---------------- getters ----------------
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getDest() { return dest; }
    public String getRole() { return role; }
    public String getPassword() { return password; }
    public boolean isVerified() { return verified; }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
    public void setPassword(String password) {
    	this.password = password;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setRefreshTokenExpiry(LocalDateTime expiry) {
        this.refreshTokenExpiry = expiry;
    }
    
 // ----- ROLE -----
  
    public void setRole(String role) {
        this.role = role;
    }

    // ----- ACTIVE -----
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


	
}
