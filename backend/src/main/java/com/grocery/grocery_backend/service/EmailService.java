package com.grocery.grocery_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOtp(String toEmail, String name, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Smart Grocery - Email Verification OTP");
        message.setText(
                "Hi " + name + ",\n\n" +
                        "Your OTP for email verification is: " + otp + "\n\n" +
                        "This OTP is valid for 10 minutes.\n\n" +
                        "- Smart Grocery Team"
        );
        mailSender.send(message);
    }

    public void sendOrderConfirmation(String toEmail,
                                      String orderId,
                                      double finalAmount) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Smart Grocery — Order Confirmed!");
        message.setText(
                "Your order has been placed successfully!\n\n" +
                        "Order ID : " + orderId + "\n" +
                        "Amount   : ₹" + String.format("%.2f", finalAmount) + "\n\n" +
                        "We will deliver your order soon.\n\n" +
                        "- Smart Grocery Team"
        );
        mailSender.send(message);
    }
}
