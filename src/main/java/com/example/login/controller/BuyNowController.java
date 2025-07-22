package com.example.login.controller;

import com.example.login.dto.BuyNowRequest;
import com.example.login.model.Transaction;
import com.example.login.repository.TransactionRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

@RestController
@RequestMapping("/api")
public class BuyNowController {

    private final TransactionRepository transactionRepository;

    public BuyNowController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @PostMapping("/buy-now")
    public ResponseEntity<String> buyNow(@RequestBody BuyNowRequest request) {
        String upiUrl = generateUpiUrl(request);
        String qrCodeBase64 = generateQrCode(upiUrl);

        // Save transaction with status PENDING
        Transaction txn = new Transaction(
                request.getUsername(),
                request.getProductName(),
                request.getQuantity(),
                request.getPrice(),
                "PENDING"
        );
        transactionRepository.save(txn);

        return ResponseEntity.ok(qrCodeBase64);
    }

    private String generateUpiUrl(BuyNowRequest req) {
        // Replace this UPI ID with your merchant UPI
        String upiId = "7708799791@pthdfc";
        String txnNote = "Buy " + req.getProductName();
        double amount = req.getPrice() * req.getQuantity();

        return String.format(
                "upi://pay?pa=%s&pn=%s&tn=%s&am=%.2f&cu=INR",
                upiId,
                req.getUsername(),
                txnNote,
                amount
        );
    }

    private String generateQrCode(String text) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix matrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 300, 300);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", pngOutputStream);

            byte[] pngData = pngOutputStream.toByteArray();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(pngData);

        } catch (WriterException | java.io.IOException e) {
            throw new RuntimeException("Error generating QR code", e);
        }
    }
}
