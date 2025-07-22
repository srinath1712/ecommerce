package com.example.login.repository;

import com.example.login.model.LoginUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<LoginUser, Long> {
    Optional<LoginUser> findByUsername(String username);
    Optional<LoginUser> findByMailId(String mailId);

}
