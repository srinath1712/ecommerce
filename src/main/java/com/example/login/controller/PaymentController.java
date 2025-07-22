package com.example.login.controller;

import com.example.login.model.Transaction;
import com.example.login.model.LoginUser;
import com.example.login.repository.TransactionRepository;
import com.example.login.repository.UserRepository;
import com.example.login.service.MailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PaymentController {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final MailService mailService;

    @Autowired
    public PaymentController(TransactionRepository transactionRepository,
                             UserRepository userRepository,
                             MailService mailService) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    @PostMapping("/payment-success/{id}")
    public ResponseEntity<String> markPaymentSuccess(@PathVariable Long id) {
        Optional<Transaction> txnOpt = transactionRepository.findById(id);
        if (txnOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transaction not found");
        }

        Transaction txn = txnOpt.get();
        txn.setPaymentStatus("SUCCESS");
        transactionRepository.save(txn);

        Optional<LoginUser> userOpt = userRepository.findByUsername(txn.getUsername());
        if (userOpt.isPresent()) {
            String email = userOpt.get().getMailId();
            mailService.sendPaymentConfirmation(
                    email,
                    txn.getUsername(),
                    txn.getProductName(),
                    txn.getQuantity(),
                    txn.getTotalAmount()
            );
        }

        return ResponseEntity.ok("Payment marked as SUCCESS and email sent.");
    }
}
