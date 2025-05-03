package com.mehdi.twosidedplatform.service.implementations;

import com.mehdi.twosidedplatform.converter.StringToRoleConverter;
import com.mehdi.twosidedplatform.dto.AuthRequest;
import com.mehdi.twosidedplatform.dto.RegisterRequest;
import com.mehdi.twosidedplatform.entity.User;
import com.mehdi.twosidedplatform.repository.UserRepository;
import com.mehdi.twosidedplatform.security.JwtUtil;
import com.mehdi.twosidedplatform.service.interfaces.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private StringToRoleConverter stringToRoleConverter;

    @Override
    public void register(RegisterRequest request) {
        try {
            logger.info("Registering user with email: {}", request.getEmail());
            if (userRepository.existsByEmail(request.getEmail())) {
                logger.warn("Attempt to register with existing email: {}", request.getEmail());
                throw new IllegalArgumentException("Email already in use");
            }

            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setFullName(request.getFullName());
            user.setRole(stringToRoleConverter.convert(request.getRole()));
            userRepository.save(user);
            logger.info("User registered successfully with email: {}", request.getEmail());
        } catch (Exception e) {
            logger.error("Error during registration for email: {}", request.getEmail(), e);
            throw new RuntimeException("Registration failed", e);
        }
    }

    @Override
    public String login(AuthRequest request) {
        try {
            logger.info("User attempting login with email: {}", request.getEmail());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            String token = jwtUtil.generateToken(request.getEmail());
            logger.info("Login successful for email: {}", request.getEmail());
            return token;
        } catch (BadCredentialsException e) {
            logger.warn("Invalid credentials for email: {}", request.getEmail());
            throw new RuntimeException("Invalid email or password", e);
        } catch (Exception e) {
            logger.error("Error during login for email: {}", request.getEmail(), e);
            throw new RuntimeException("Login failed", e);
        }
    }
}
