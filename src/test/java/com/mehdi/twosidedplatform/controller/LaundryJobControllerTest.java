package com.mehdi.twosidedplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mehdi.twosidedplatform.dto.LaundryJobDto;
import com.mehdi.twosidedplatform.repository.UserRepository;
import com.mehdi.twosidedplatform.security.JwtAuthFilter;
import com.mehdi.twosidedplatform.security.JwtUtil;
import com.mehdi.twosidedplatform.security.UserDetailsServiceImpl;
import com.mehdi.twosidedplatform.service.interfaces.LaundryJobService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LaundryJobController.class)
@Import(LaundryJobControllerTest.TestConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class LaundryJobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LaundryJobService laundryJobService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public LaundryJobService laundryJobService() {
            return Mockito.mock(LaundryJobService.class);
        }

        @Bean
        public JwtAuthFilter jwtAuthFilter() {
            return new JwtAuthFilter(jwtUtil(), userDetailsService());
        }

        @Bean
        public JwtUtil jwtUtil() {
            return Mockito.mock(JwtUtil.class);
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
    @DisplayName("Assign job successfully")
    @WithMockUser(roles = {"LAUNDRY_WORKER"})
    void assignJob_ShouldReturnCreatedJob() throws Exception {
        LaundryJobDto requestDto = new LaundryJobDto();
        requestDto.setRequestId(1L);
        requestDto.setWorkerId(2L);
        requestDto.setPickupTime(LocalDateTime.now().plusDays(1));
        requestDto.setDropoffTime(LocalDateTime.now().plusDays(2));
        requestDto.setPaid(false);

        LaundryJobDto responseDto = new LaundryJobDto();
        responseDto.setId(1L);
        responseDto.setRequestId(1L);
        responseDto.setWorkerId(2L);
        responseDto.setPickupTime(requestDto.getPickupTime());
        responseDto.setDropoffTime(requestDto.getDropoffTime());
        responseDto.setPaid(false);

        when(laundryJobService.assignJob(any())).thenReturn(responseDto);

        mockMvc.perform(post("/api/laundry-jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("Get jobs by worker ID")
    @WithMockUser(roles = {"LAUNDRY_WORKER"})
    void getByWorkerId_ShouldReturnList() throws Exception {
        Long workerId = 2L;

        LaundryJobDto dto = new LaundryJobDto();
        dto.setId(1L);
        dto.setWorkerId(workerId);
        dto.setRequestId(1L);
        dto.setPickupTime(LocalDateTime.now().plusDays(1));
        dto.setDropoffTime(LocalDateTime.now().plusDays(2));
        dto.setPaid(false);

        when(laundryJobService.getJobsByWorkerId(workerId)).thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/api/laundry-jobs/worker/{workerId}", workerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].workerId").value(workerId));
    }

    @Test
    @DisplayName("Update paid status")
    @WithMockUser(roles = {"LAUNDRY_WORKER"})
    void updatePaidStatus_ShouldUpdateJob() throws Exception {
        Long jobId = 1L;

        LaundryJobDto dto = new LaundryJobDto();
        dto.setId(jobId);
        dto.setPaid(true);
        dto.setRequestId(1L);
        dto.setWorkerId(2L);
        dto.setPickupTime(LocalDateTime.now().plusDays(1));
        dto.setDropoffTime(LocalDateTime.now().plusDays(2));

        when(laundryJobService.updateJobStatus(jobId, true)).thenReturn(dto);

        mockMvc.perform(put("/api/laundry-jobs/{jobId}/paid", jobId)
                        .param("paid", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paid").value(true));
    }
}
