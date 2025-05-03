package com.mehdi.twosidedplatform.service.implementations;

import com.mehdi.twosidedplatform.dto.LaundryRequestDto;
import com.mehdi.twosidedplatform.entity.LaundryRequest;
import com.mehdi.twosidedplatform.entity.enums.LaundryRequestStatus;
import com.mehdi.twosidedplatform.entity.User;
import com.mehdi.twosidedplatform.repository.LaundryRequestRepository;
import com.mehdi.twosidedplatform.repository.UserRepository;
import com.mehdi.twosidedplatform.service.interfaces.LaundryRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LaundryRequestServiceImpl implements LaundryRequestService {
    private static final Logger logger = LoggerFactory.getLogger(LaundryRequestServiceImpl.class);

    @Autowired
    private LaundryRequestRepository laundryRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public LaundryRequestDto createRequest(LaundryRequestDto dto) {
        try {
            logger.info("Creating laundry request for customer ID: {}", dto.getCustomerId());
            Optional<User> userOpt = userRepository.findById(dto.getCustomerId());
            if (userOpt.isEmpty()) {
                logger.error("Customer with ID {} not found", dto.getCustomerId());
                throw new IllegalArgumentException("Customer not found with ID: " + dto.getCustomerId());
            }

            User customer = userOpt.get();

            LaundryRequest request = new LaundryRequest();
            request.setCustomer(customer);
            request.setAddress(dto.getAddress());
            request.setPreferredPickupTime(dto.getPreferredPickupTime());
            request.setSpecialInstructions(dto.getSpecialInstructions());
            request.setStatus(LaundryRequestStatus.NEW);

            LaundryRequest savedRequest = laundryRequestRepository.save(request);
            logger.info("Laundry request created successfully with ID: {}", savedRequest.getId());

            dto.setId(savedRequest.getId());
            dto.setStatus(savedRequest.getStatus());

            return dto;
        } catch (Exception e) {
            logger.error("Error while creating laundry request", e);
            throw new RuntimeException("Failed to create laundry request", e);
        }
    }

    @Override
    public List<LaundryRequestDto> getRequestsByCustomerId(Long customerId) {
        try {
            logger.info("Fetching laundry requests for customer ID: {}", customerId);
            List<LaundryRequestDto> results = laundryRequestRepository.findAll().stream()
                    .filter(r -> r.getCustomer().getId().equals(customerId))
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            logger.info("Found {} requests for customer ID: {}", results.size(), customerId);
            return results;
        } catch (Exception e) {
            logger.error("Error fetching requests for customer ID: {}", customerId, e);
            throw new RuntimeException("Failed to fetch requests by customer ID", e);
        }
    }

    @Override
    public List<LaundryRequestDto> getAllRequests() {
        try {
            logger.info("Fetching all laundry requests");
            List<LaundryRequestDto> results = laundryRequestRepository.findAll().stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            logger.info("Fetched {} laundry requests in total", results.size());
            return results;
        } catch (Exception e) {
            logger.error("Error fetching all laundry requests", e);
            throw new RuntimeException("Failed to fetch all requests", e);
        }
    }

    private LaundryRequestDto mapToDto(LaundryRequest r) {
        LaundryRequestDto dto = new LaundryRequestDto();
        dto.setId(r.getId());
        dto.setCustomerId(r.getCustomer().getId());
        dto.setAddress(r.getAddress());
        dto.setPreferredPickupTime(r.getPreferredPickupTime());
        dto.setSpecialInstructions(r.getSpecialInstructions());
        dto.setStatus(r.getStatus());
        return dto;
    }
}
