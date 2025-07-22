package com.example.login.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    // Single map keyed by "email:purpose"
    private final Map<String, String> otpMap = new HashMap<>();

    public String generateOtp(String email, String purpose) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        String key = email + ":" + purpose;
        otpMap.put(key, otp);

        System.out.println("Generated OTP for " + key + " = " + otp); // Debug
        return otp;
    }

    public boolean verifyOtp(String email, String otp, String purpose) {
        String key = email + ":" + purpose;
        String storedOtp = otpMap.get(key);

        System.out.println("Verifying OTP for " + key + ", Input: " + otp + ", Stored: " + storedOtp); // Debug
        return otp != null && otp.equals(storedOtp);
    }

    public void clearOtp(String email) {
        otpMap.keySet().removeIf(k -> k.startsWith(email + ":"));
    }
}
