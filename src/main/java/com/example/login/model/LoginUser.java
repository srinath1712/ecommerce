package com.example.login.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class LoginUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "mail_id", unique = true, nullable = false)
    private String mailId;

    public LoginUser() {
        // default constructor for JPA
    }

    public LoginUser(String username, String password, String mailId) {
        this.username = username;
        this.password = password;
        this.mailId = mailId;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }
}
