package com.mehdi.twosidedplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mehdi.twosidedplatform.dto.LaundryRequestDto;
import com.mehdi.twosidedplatform.entity.enums.LaundryRequestStatus;
import com.mehdi.twosidedplatform.repository.UserRepository;
import com.mehdi.twosidedplatform.security.JwtUtil;
import com.mehdi.twosidedplatform.security.UserDetailsServiceImpl;
import com.mehdi.twosidedplatform.service.interfaces.LaundryRequestService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LaundryRequestController.class)
@Import(LaundryRequestControllerTest.TestConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class LaundryRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LaundryRequestService laundryRequestService;

    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(authorities = "CUSTOMER")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Test
    void createRequest_ShouldReturnCreatedDto() throws Exception {
        LaundryRequestDto dto = new LaundryRequestDto();
        dto.setCustomerId(10L);
        dto.setAddress("123 Main St");
        dto.setSpecialInstructions("Handle with care");
        dto.setPreferredPickupTime(LocalDateTime.now().plusDays(1));

        LaundryRequestDto expected = new LaundryRequestDto();
        expected.setId(1L);
        expected.setCustomerId(10L);
        expected.setAddress("123 Main St");
        expected.setSpecialInstructions("Handle with care");
        expected.setPreferredPickupTime(dto.getPreferredPickupTime());
        expected.setStatus(LaundryRequestStatus.NEW);

        Mockito.when(laundryRequestService.createRequest(any())).thenReturn(expected);

        mockMvc.perform(post("/api/laundry-requests").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.status").value("NEW"));
    }

    @Test
    @DisplayName("Should return 400 when pickup time is in the past")
    @WithMockUser(roles = {"CUSTOMER"})
    void createRequest_InvalidPickupTime_ShouldReturnBadRequest() throws Exception {
        LaundryRequestDto dto = new LaundryRequestDto();
        dto.setCustomerId(10L);
        dto.setAddress("123 Main St");
        dto.setSpecialInstructions("Handle with care");


        mockMvc.perform(post("/api/laundry-requests").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto))).andExpect(status().isBadRequest()).andExpect(jsonPath("$.preferredPickupTime").value("Preferred pickup time is required"));
    }

    @Test
    @DisplayName("Should return list of requests for a customer")
    @WithMockUser(roles = {"CUSTOMER"})
    void getByCustomer_ShouldReturnList() throws Exception {
        Long customerId = 10L;

        LaundryRequestDto dto = new LaundryRequestDto();
        dto.setId(1L);
        dto.setCustomerId(customerId);
        dto.setAddress("123 Main St");
        dto.setSpecialInstructions("None");
        dto.setPreferredPickupTime(LocalDateTime.now().plusDays(1));
        dto.setStatus(LaundryRequestStatus.NEW);

        when(laundryRequestService.getRequestsByCustomerId(customerId)).thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/api/laundry-requests/customer/{customerId}", customerId)).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(1L)).andExpect(jsonPath("$[0].customerId").value(customerId));
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public LaundryRequestService laundryRequestService() {
            return Mockito.mock(LaundryRequestService.class);
        }

        @Bean
        public JwtUtil jwtUtil() {
            return Mockito.mock(JwtUtil.class);
        }

        @Bean
        public UserDetailsServiceImpl userDetailsService() {
            return new UserDetailsServiceImpl(userRepository());
        }

        @Bean
        public UserRepository userRepository() {
            return Mockito.mock(UserRepository.class);
        }
    }
}
