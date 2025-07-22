package com.example.login.controller;

import com.example.login.dto.AddCartRequest;
import com.example.login.dto.CartResponse;
import com.example.login.model.CartItem;
import com.example.login.model.LoginUser;
import com.example.login.repository.CartItemRepository;
import com.example.login.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    public CartController(CartItemRepository cartItemRepository, UserRepository userRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(@RequestBody AddCartRequest request) {
        Optional<LoginUser> userOpt = userRepository.findByUsername(request.getUsername());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CartResponse("User not found"));
        }

        LoginUser user = userOpt.get();

        CartItem item = new CartItem(request.getProductName(), request.getQuantity(), request.getPrice(), user);
        cartItemRepository.save(item);

        logger.info("Added item to cart: {}", request.getProductName());

        return ResponseEntity.ok(new CartResponse("Item added to cart"));
    }

    @GetMapping("/view/{username}")
    public ResponseEntity<List<CartItem>> viewCart(@PathVariable String username) {
        Optional<LoginUser> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<CartItem> cartItems = cartItemRepository.findByUser(userOpt.get());
        return ResponseEntity.ok(cartItems);
    }
}
