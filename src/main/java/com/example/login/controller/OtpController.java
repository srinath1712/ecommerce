package com.example.login.controller;

import com.example.login.dto.OtpRequest;
import com.example.login.dto.OtpVerificationRequest;
import com.example.login.model.LoginUser;
import com.example.login.repository.UserRepository;
import com.example.login.service.MailService;
import com.example.login.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpService otpService;

    @Autowired
    private MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(@RequestBody OtpRequest request) {
        String email = request.getMailId();
        String purpose = request.getPurpose();

        if (userRepository.findByMailId(email).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
        }

        String otp = otpService.generateOtp(email, purpose);
        mailService.sendOtp(email, otp, purpose);
        return ResponseEntity.ok("OTP sent for " + purpose);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerificationRequest request) {
        String email = request.getMailId();
        String otp = request.getOtp();
        String purpose = request.getPurpose();

        if (!otpService.verifyOtp(email, otp, purpose)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired OTP");
        }

        // Example use case: password reset
        if ("password_reset".equalsIgnoreCase(purpose)) {
            Optional<LoginUser> userOpt = userRepository.findByMailId(email);
            if (userOpt.isPresent()) {
                LoginUser user = userOpt.get();
                user.setPassword(request.getNewPassword());
                userRepository.save(user);
            }
        }

        otpService.clearOtp(email);
        return ResponseEntity.ok("OTP verified and processed for: " + purpose);
    }
}
