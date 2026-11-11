package com.properties.real_estate_spring_boot_app.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.properties.real_estate_spring_boot_app.dto.BookingRequest;
import com.properties.real_estate_spring_boot_app.model.Booking;
import com.properties.real_estate_spring_boot_app.model.BookingStatus;
import com.properties.real_estate_spring_boot_app.repository.BookingRepository;
import com.properties.real_estate_spring_boot_app.service.BookingService;
import com.properties.real_estate_spring_boot_app.service.EmailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class PublicBookingController {

	private final BookingService bookingService;

	private final EmailService emailService;

	// 🔓 CREATE BOOKING (USER)
//    @PostMapping
//    public List<Booking> create(@RequestBody BookingRequest request) {
//        return bookingService.getAllBookings();
//    }
	private final BookingRepository bookingRepo;

	@PostMapping
	public ResponseEntity<?> createBooking(@RequestBody BookingRequest req) {

		Booking booking = new Booking();

		booking.setUserId(req.getUserId());
		booking.setName(req.getName());
		booking.setEmail(req.getEmail());
		booking.setPhone(req.getPhone());

		booking.setPropertyId(req.getPropertyId());
		booking.setPropertyTitle(req.getPropertyTitle());
		booking.setPropertyImage(req.getPropertyImage());

		booking.setVisitDate(req.getVisitDate());
		booking.setVisitTime(req.getVisitTime());

		booking.setMessage(req.getMessage());
		booking.setStatus(BookingStatus.PENDING);
		booking.setCreatedAt(LocalDateTime.now());

		Booking savedBooking = bookingRepo.save(booking);

	
			// Mail send for booking request
			emailService.bookingRequestMail(booking.getEmail(), booking.getName(), booking.getPropertyTitle());
			
		return ResponseEntity.ok(savedBooking);
	}

	// 🔓 USER BOOKINGS
	@GetMapping("/user/{email}")
	public List<Booking> userBookings(@PathVariable String email) {
		return bookingRepo.findByEmail(email);
	}

	// Delete Bookings
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
		bookingRepo.deleteById(id);
		return ResponseEntity.ok("Booking cancelled");
	}

}
