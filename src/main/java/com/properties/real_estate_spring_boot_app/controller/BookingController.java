package com.properties.real_estate_spring_boot_app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.properties.real_estate_spring_boot_app.model.Booking;
import com.properties.real_estate_spring_boot_app.service.BookingService;

@RestController
@RequestMapping("/bookings")
@PreAuthorize("hasRole('ADMIN')")
public class BookingController {

    @Autowired
    private BookingService bookingService;
    
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<Booking> all() {
        return bookingService.getAllBookings();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookingService.deleteBooking(id);
    }
}
