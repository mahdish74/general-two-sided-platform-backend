package com.mehdi.twosidedplatform.service.implementations;

import com.mehdi.twosidedplatform.dto.AuthRequest;
import com.mehdi.twosidedplatform.dto.RegisterRequest;
import com.mehdi.twosidedplatform.entity.User;
import com.mehdi.twosidedplatform.repository.UserRepository;
import com.mehdi.twosidedplatform.security.JwtUtil;
import com.mehdi.twosidedplatform.service.interfaces.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void register(RegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setRole(request.getRole());
        userRepository.save(user);
    }

    @Override
    public String login(AuthRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        return jwtUtil.generateToken(request.getEmail());
    }
}
