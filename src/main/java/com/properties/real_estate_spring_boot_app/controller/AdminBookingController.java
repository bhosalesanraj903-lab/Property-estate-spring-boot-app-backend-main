package com.properties.real_estate_spring_boot_app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.properties.real_estate_spring_boot_app.model.Booking;
import com.properties.real_estate_spring_boot_app.model.BookingStatus;
import com.properties.real_estate_spring_boot_app.repository.BookingRepository;
import com.properties.real_estate_spring_boot_app.service.EmailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/bookings")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminBookingController {

    private final BookingRepository bookingRepo;
    
    private final EmailService emailService;

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingRepo.findAll();
    }

    @PutMapping("/{id}/status")
    public Booking updateStatus(
            @PathVariable Long id,
            @RequestParam BookingStatus status
    ) {
        Booking booking = bookingRepo.findById(id).orElseThrow();
        booking.setStatus(status);
        
        if(status == BookingStatus.REJECTED) {
        		// Mail send for rejected booking
			emailService.bookingRejectedMail(booking.getEmail(), booking.getName(), booking.getPropertyTitle());
		}else {
			// Mail send for confirmed booking
		    emailService.bookingConfirmedMail(booking.getEmail(), booking.getName(), booking.getPropertyTitle());
		}
     return bookingRepo.save(booking);
    }
}
