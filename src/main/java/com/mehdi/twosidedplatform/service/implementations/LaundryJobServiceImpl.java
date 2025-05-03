package com.mehdi.twosidedplatform.service.implementations;

import com.mehdi.twosidedplatform.dto.LaundryJobDto;
import com.mehdi.twosidedplatform.entity.LaundryJob;
import com.mehdi.twosidedplatform.entity.LaundryRequest;
import com.mehdi.twosidedplatform.entity.enums.LaundryRequestStatus;
import com.mehdi.twosidedplatform.entity.User;
import com.mehdi.twosidedplatform.exception.ResourceNotFoundException;
import com.mehdi.twosidedplatform.repository.LaundryJobRepository;
import com.mehdi.twosidedplatform.repository.LaundryRequestRepository;
import com.mehdi.twosidedplatform.repository.UserRepository;
import com.mehdi.twosidedplatform.service.interfaces.LaundryJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LaundryJobServiceImpl implements LaundryJobService {

    private static final Logger logger = LoggerFactory.getLogger(LaundryJobServiceImpl.class);

    @Autowired
    private LaundryJobRepository laundryJobRepository;

    @Autowired
    private LaundryRequestRepository laundryRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public LaundryJobDto assignJob(LaundryJobDto dto) {
        logger.info("Assigning job: requestId={}, workerId={}", dto.getRequestId(), dto.getWorkerId());

        LaundryRequest request = laundryRequestRepository.findById(dto.getRequestId())
                .orElseThrow(() -> {
                    logger.error("LaundryRequest not found: id={}", dto.getRequestId());
                    return new ResourceNotFoundException("LaundryRequest not found with id: " + dto.getRequestId());
                });

        User worker = userRepository.findById(dto.getWorkerId())
                .orElseThrow(() -> {
                    logger.error("Worker not found: id={}", dto.getWorkerId());
                    return new ResourceNotFoundException("Worker not found with id: " + dto.getWorkerId());
                });

        LaundryJob job = new LaundryJob();
        job.setRequest(request);
        job.setWorker(worker);
        job.setPickupTime(dto.getPickupTime());
        job.setDropoffTime(dto.getDropoffTime());
        job.setPaid(dto.isPaid());

        request.setStatus(LaundryRequestStatus.ASSIGNED);

        laundryJobRepository.save(job);
        dto.setId(job.getId());

        logger.info("Job assigned successfully: jobId={}", job.getId());
        return dto;
    }

    @Override
    public List<LaundryJobDto> getJobsByWorkerId(Long workerId) {
        logger.info("Fetching jobs for workerId={}", workerId);
        return laundryJobRepository.findAll().stream()
                .filter(j -> j.getWorker().getId().equals(workerId))
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public LaundryJobDto updateJobStatus(Long jobId, boolean paid) {
        logger.info("Updating job status: jobId={}, paid={}", jobId, paid);
        LaundryJob job = laundryJobRepository.findById(jobId)
                .orElseThrow(() -> {
                    logger.error("LaundryJob not found: id={}", jobId);
                    return new ResourceNotFoundException("LaundryJob not found with id: " + jobId);
                });

        job.setPaid(paid);
        laundryJobRepository.save(job);
        logger.info("Updated job status successfully: jobId={}", jobId);

        return mapToDto(job);
    }

    private LaundryJobDto mapToDto(LaundryJob job) {
        LaundryJobDto dto = new LaundryJobDto();
        dto.setId(job.getId());
        dto.setRequestId(job.getRequest().getId());
        dto.setWorkerId(job.getWorker().getId());
        dto.setPickupTime(job.getPickupTime());
        dto.setDropoffTime(job.getDropoffTime());
        dto.setPaid(job.isPaid());
        return dto;
    }
}
