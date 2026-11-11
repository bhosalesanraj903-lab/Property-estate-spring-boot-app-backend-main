package com.properties.real_estate_spring_boot_app.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class BookingRequest {

    private Long userId;
    private String name;
    private String email;
    private String phone;

    private Long propertyId;
    private String propertyTitle;
    private String propertyImage;

    private LocalDate visitDate;
    private LocalTime visitTime;

    private String message;

    // ✅ GETTERS & SETTERS (REQUIRED)

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Long getPropertyId() { return propertyId; }
    public void setPropertyId(Long propertyId) { this.propertyId = propertyId; }

    public String getPropertyTitle() { return propertyTitle; }
    public void setPropertyTitle(String propertyTitle) { this.propertyTitle = propertyTitle; }

    public String getPropertyImage() { return propertyImage; }
    public void setPropertyImage(String propertyImage) { this.propertyImage = propertyImage; }

    public LocalDate getVisitDate() { return visitDate; }
    public void setVisitDate(LocalDate visitDate) { this.visitDate = visitDate; }

    public LocalTime getVisitTime() { return visitTime; }
    public void setVisitTime(LocalTime visitTime) { this.visitTime = visitTime; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
