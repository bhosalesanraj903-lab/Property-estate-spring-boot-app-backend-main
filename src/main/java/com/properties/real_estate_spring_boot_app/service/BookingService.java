package com.properties.real_estate_spring_boot_app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.properties.real_estate_spring_boot_app.model.Booking;
import com.properties.real_estate_spring_boot_app.repository.BookingRepository;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> getAllBookings() {
        List<Booking> list = new ArrayList<>();
        bookingRepository.findAll().forEach(list::add);
        return list;
    }

    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Booking not found");
        }
        bookingRepository.deleteById(id);
    }



	public List<Booking> getBookingsByUser(Long userId) {
		if (!bookingRepository.existsById(userId)) {
            throw new RuntimeException("Bookingfound");
        }
       return bookingRepository.findByUserId(userId);
	}
}
