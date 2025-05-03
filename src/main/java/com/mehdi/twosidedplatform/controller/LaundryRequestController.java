package com.mehdi.twosidedplatform.controller;

import com.mehdi.twosidedplatform.dto.LaundryRequestDto;
import com.mehdi.twosidedplatform.service.interfaces.LaundryRequestService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/laundry-requests")
public class LaundryRequestController {

    private static final Logger logger = LoggerFactory.getLogger(LaundryRequestController.class);

    @Autowired
    private LaundryRequestService laundryRequestService;

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @PostMapping
    public LaundryRequestDto create(@Valid @RequestBody LaundryRequestDto dto) {
        logger.info("Creating laundry request for customerId={}", dto.getCustomerId());
        try {
            LaundryRequestDto created = laundryRequestService.createRequest(dto);
            logger.info("Successfully created laundry request with ID={}", created.getId());
            return created;
        } catch (Exception e) {
            logger.error("Failed to create laundry request for customerId={}", dto.getCustomerId(), e);
            throw e;
        }
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @GetMapping("/customer/{customerId}")
    public List<LaundryRequestDto> getByCustomer(@PathVariable Long customerId) {
        logger.info("Fetching laundry requests for customerId={}", customerId);
        try {
            List<LaundryRequestDto> requests = laundryRequestService.getRequestsByCustomerId(customerId);
            logger.info("Found {} request(s) for customerId={}", requests.size(), customerId);
            return requests;
        } catch (Exception e) {
            logger.error("Failed to fetch requests for customerId={}", customerId, e);
            throw e;
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<LaundryRequestDto> getAll() {
        logger.info("Fetching all laundry requests");
        try {
            List<LaundryRequestDto> all = laundryRequestService.getAllRequests();
            logger.info("Fetched {} total request(s)", all.size());
            return all;
        } catch (Exception e) {
            logger.error("Failed to fetch all laundry requests", e);
            throw e;
        }
    }
}
