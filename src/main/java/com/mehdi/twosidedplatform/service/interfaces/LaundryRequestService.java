package com.mehdi.twosidedplatform.service.interfaces;

import com.mehdi.twosidedplatform.dto.LaundryRequestDto;

import java.util.List;

public interface LaundryRequestService {
    LaundryRequestDto createRequest(LaundryRequestDto dto);
    List<LaundryRequestDto> getRequestsByCustomerId(Long customerId);
    List<LaundryRequestDto> getAllRequests();
}
