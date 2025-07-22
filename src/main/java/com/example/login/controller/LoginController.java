    package com.example.login.controller;

//    import com.example.login.dto.ForgotPasswordRequest;
    import com.example.login.dto.LoginResponse;
    import com.example.login.model.LoginUser;
    import com.example.login.repository.UserRepository;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.http.*;
    import org.springframework.web.bind.annotation.*;

    import java.util.Optional;

    @RestController
    @RequestMapping("/api")
    public class LoginController {

        private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
        private final UserRepository userRepository;

        public LoginController(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        @PostMapping("/login")
        public ResponseEntity<LoginResponse> login(@RequestBody LoginUser request) {
            String username = request.getUsername();
            String password = request.getPassword();

            logger.info("Login attempt for user: {}", username);

            Optional<LoginUser> userOpt = userRepository.findByUsername(username);

            if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
                LoginUser user = userOpt.get();
                logger.info("Login successful for user: {}", username);
                return ResponseEntity.ok(new LoginResponse("Login successful", user.getMailId()));
            }
            else {
                logger.warn("Login failed for user: {}", username);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse("Invalid credentials",null));
            }
        }


        @PostMapping("/register")
        public ResponseEntity<LoginResponse> register(@RequestBody LoginUser request) {
            String username = request.getUsername();
            String password = request.getPassword();
            String mailId = request.getMailId();

            logger.info("Register attempt for user: {}", username);

            // Check if username or mail already exists
            boolean usernameExists = userRepository.findByUsername(username).isPresent();
            boolean mailExists = userRepository.findByMailId(mailId).isPresent();

            if (usernameExists || mailExists) {
                logger.warn("Registration failed - Username or Email already exists for user: {}", username);
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new LoginResponse("Username or Email already exists", null));
            }

            // Save new user
            LoginUser newUser = new LoginUser(username, password, mailId);
            userRepository.save(newUser);
            logger.info("Registration successful for user: {}", username);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new LoginResponse("User registered successfully", mailId));
        }

//        @PostMapping("/forget-password")
//        public ResponseEntity<LoginResponse> resetPassword(@RequestBody ForgotPasswordRequest request) {
//            String mailId = request.getMailId();
//            String newPassword = request.getNewPassword();
//
//            logger.info("Password reset attempt for email: {}", mailId);
//
//            Optional<LoginUser> userOpt = userRepository.findByMailId(mailId);
//
//            if (userOpt.isPresent()) {
//                LoginUser user = userOpt.get();
//                user.setPassword(newPassword);
//                userRepository.save(user);
//                logger.info("Password successfully updated for user with email: {}", mailId);
//                return ResponseEntity.ok(new LoginResponse("Password updated successfully", mailId));
//            } else {
//                logger.warn("Password reset failed - Email not found: {}", mailId);
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body(new LoginResponse("Email not found", null));
//            }
//        }

    }
