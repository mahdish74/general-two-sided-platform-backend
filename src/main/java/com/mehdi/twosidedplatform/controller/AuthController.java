package com.mehdi.twosidedplatform.controller;


import com.mehdi.twosidedplatform.dto.AuthRequest;
import com.mehdi.twosidedplatform.dto.AuthResponse;
import com.mehdi.twosidedplatform.dto.RegisterRequest;
import com.mehdi.twosidedplatform.service.interfaces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest request) {
        authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        String token = authService.login(request);
        return new AuthResponse(token);
    }
}
