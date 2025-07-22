package com.example.login.repository;

import com.example.login.model.CartItem;
import com.example.login.model.LoginUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(LoginUser user);
}
