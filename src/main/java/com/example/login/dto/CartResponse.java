package com.example.login.dto;

public class CartResponse {
    private String message;

    public CartResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
