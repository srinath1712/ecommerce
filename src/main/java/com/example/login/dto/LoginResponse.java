package com.example.login.dto;

public class LoginResponse {
    private String message;
    private String mailId;

    public LoginResponse(String message, String mailId) {
        this.message = message;
        this.mailId = mailId;
    }

    public String getMessage() {
        return message;
    }

    public String getMailId() {
        return mailId;
    }

}
