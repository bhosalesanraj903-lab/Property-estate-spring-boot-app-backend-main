package com.properties.real_estate_spring_boot_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Value("${app.mail.from}")
	private String fromAddress;

	public void sendOtp(String email, String otp) {

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(fromAddress);
		msg.setTo(email);
		msg.setSubject("Property Dealer OTP");
		msg.setText("Your OTP is " + otp + ". It is valid for 5 minutes.");

		mailSender.send(msg);

		System.out.println("EMAIL OTP SENT TO: " + email);
	}

	public void bookingConfirmedMail(String toEmail, String name, String propertyTitle) {

		SimpleMailMessage mailMessage = new SimpleMailMessage();

		mailMessage.setFrom(fromAddress);

		mailMessage.setTo(toEmail);

		mailMessage.setSubject("Booking Confirmation");

		mailMessage.setText("Dear " + name + ",\n\n" + "Thank you for booking a visit to " + propertyTitle
				+ ". We look forward to welcoming you!\n\n" + "Best regards,\n" + "Real Estate Team");

		mailSender.send(mailMessage);
	}
	
	public void bookingRejectedMail(String toEmail, String name, String propertyTitle) {

		SimpleMailMessage mailMessage = new SimpleMailMessage();

		mailMessage.setFrom(fromAddress);

		mailMessage.setTo(toEmail);

		mailMessage.setSubject("Booking Confirmation");

		mailMessage.setText("Dear " + name + ",\n\n" + "Thank you for booking a visit to " + propertyTitle
				+ "but unfortunately we are unable to confirm your booking at this time. Please contact us for more information.\n\n" + "Best regards,\n" + "Real Estate Team");

		mailSender.send(mailMessage);
	}
	
	public void bookingRequestMail(String toEmail, String name, String propertyTitle) {

		SimpleMailMessage mailMessage = new SimpleMailMessage();

		mailMessage.setFrom(fromAddress);

		mailMessage.setTo(toEmail);

		mailMessage.setSubject("Booking Request Received");

		mailMessage.setText("Dear " + name + ",\n\n" + "Thank you for booking a visit to " + propertyTitle
				+ "We have received your booking request and will review it shortly. You will receive a confirmation email once your booking is approved.\n\n" + "Best regards,\n" + "Real Estate Team");

		mailSender.send(mailMessage);
	}
}
