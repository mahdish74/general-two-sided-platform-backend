package com.mehdi.twosidedplatform.controller;

import com.mehdi.twosidedplatform.dto.AuthRequest;
import com.mehdi.twosidedplatform.dto.AuthResponse;
import com.mehdi.twosidedplatform.dto.RegisterRequest;
import com.mehdi.twosidedplatform.service.interfaces.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public void register(@RequestBody @Valid RegisterRequest request) {
        logger.info("Attempting to register user with email: {}", request.getEmail());
        try {
            authService.register(request);
            logger.info("User registered successfully: {}", request.getEmail());
        } catch (Exception e) {
            logger.error("Registration failed for email: {}", request.getEmail(), e);
            throw e; // will be handled by GlobalExceptionHandler
        }
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        logger.info("Login attempt for email: {}", request.getEmail());
        try {
            String token = authService.login(request);
            logger.info("Login successful for email: {}", request.getEmail());
            return new AuthResponse(token);
        } catch (Exception e) {
            logger.error("Login failed for email: {}", request.getEmail(), e);
            throw e;
        }
    }
}
