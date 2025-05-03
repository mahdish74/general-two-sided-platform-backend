package com.mehdi.twosidedplatform.service.interfaces;

import com.mehdi.twosidedplatform.dto.LaundryJobDto;

import java.util.List;

public interface LaundryJobService {
    LaundryJobDto assignJob(LaundryJobDto dto);
    List<LaundryJobDto> getJobsByWorkerId(Long workerId);
    LaundryJobDto updateJobStatus(Long jobId, boolean paid);
}