package com.example.login.dto;

public class BuyNowRequest {
    private String username;
    private String productName;
    private int quantity;
    private double price;

    // Constructors
    public BuyNowRequest() {}

    public BuyNowRequest(String username, String productName, int quantity, double price) {
        this.username = username;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
