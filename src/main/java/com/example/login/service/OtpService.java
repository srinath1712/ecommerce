package com.example.login.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    private final Map<String, String> otpMap = new HashMap<>();
    private final Map<String, String> otpContextMap = new HashMap<>();

    public String generateOtp(String email, String purpose) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpMap.put(email, otp);
        otpContextMap.put(email, purpose);
        return otp;
    }

    public boolean verifyOtp(String email, String otp, String purpose) {
        return otp.equals(otpMap.get(email)) && purpose.equals(otpContextMap.get(email));
    }

    public void clearOtp(String email) {
        otpMap.remove(email);
        otpContextMap.remove(email);
    }
}
