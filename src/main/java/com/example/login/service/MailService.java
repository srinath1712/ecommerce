package com.example.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPaymentConfirmation(String toEmail, String username, String productName, int quantity, double totalAmount) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Payment Confirmation - Thank You!");
        message.setText(String.format(
                "Hi %s,\n\nYour payment for %d x %s (₹%.2f) has been received successfully.\n\nThank you for shopping with us!\n\n- E-Commerce Team",
                username, quantity, productName, totalAmount
        ));
        message.setFrom("vsrinath1712@gmail.com");
        mailSender.send(message);
    }

    public void sendOtp(String email, String otp, String purpose) {
        String subject = "OTP for " + purpose;
        String text = "Your OTP for " + purpose + " is: " + otp + "\n\nValid for 5 minutes.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("vsrinath1712@gmail.com");
        mailSender.send(message);
    }
}
