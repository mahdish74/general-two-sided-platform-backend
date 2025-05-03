package com.mehdi.twosidedplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mehdi.twosidedplatform.dto.AuthRequest;
import com.mehdi.twosidedplatform.dto.RegisterRequest;
import com.mehdi.twosidedplatform.repository.UserRepository;
import com.mehdi.twosidedplatform.security.JwtAuthFilter;
import com.mehdi.twosidedplatform.security.JwtUtil;
import com.mehdi.twosidedplatform.security.UserDetailsServiceImpl;
import com.mehdi.twosidedplatform.service.interfaces.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(AuthControllerTest.TestConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {

        @Bean
        public AuthService authService() {
            return Mockito.mock(AuthService.class);
        }

        @Bean
        public JwtUtil jwtUtil() {
            return Mockito.mock(JwtUtil.class);
        }

        @Bean
        public JwtAuthFilter jwtAuthFilter(JwtUtil jwtUtil) {
            return new JwtAuthFilter(jwtUtil, userDetailsService());
        }

        @Bean
        public UserDetailsServiceImpl userDetailsService() {
            return Mockito.mock(UserDetailsServiceImpl.class);
        }

        @Bean
        public UserRepository userRepository() {
            return Mockito.mock(UserRepository.class);
        }
    }

    @Test
    @DisplayName("Should register successfully")
    void register_ValidInput_ShouldSucceed() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setPassword("securePass123");
        request.setFullName("Test User");
        request.setRole("CUSTOMER");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 400 for invalid registration")
    void register_InvalidInput_ShouldReturnBadRequest() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("bad-email");
        request.setPassword("123");
        request.setFullName("");
        request.setRole("");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Email should be valid"))
                .andExpect(jsonPath("$.password").value("Password must be at least 6 characters"))
                .andExpect(jsonPath("$.fullName").value("Full name is required"))
                .andExpect(jsonPath("$.role").value("Role is required"));
    }

    @Test
    @DisplayName("Should return JWT token on successful login")
    void login_ValidCredentials_ShouldReturnToken() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("test@example.com");
        authRequest.setPassword("securePass123");

        Mockito.when(authService.login(any())).thenReturn("fake-jwt-token");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }
}
