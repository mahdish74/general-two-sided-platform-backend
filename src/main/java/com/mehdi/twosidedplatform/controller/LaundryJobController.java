package com.mehdi.twosidedplatform.controller;

import com.mehdi.twosidedplatform.dto.LaundryJobDto;
import com.mehdi.twosidedplatform.service.interfaces.LaundryJobService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/laundry-jobs")
public class LaundryJobController {

    private static final Logger logger = LoggerFactory.getLogger(LaundryJobController.class);

    @Autowired
    private LaundryJobService laundryJobService;

    @PreAuthorize("hasRole('LAUNDRY_WORKER') or hasRole('ADMIN')")
    @PostMapping
    public LaundryJobDto assign(@Valid @RequestBody LaundryJobDto dto) {
        logger.info("Assigning job to workerId={} for requestId={}", dto.getWorkerId(), dto.getRequestId());
        try {
            LaundryJobDto result = laundryJobService.assignJob(dto);
            logger.info("Successfully assigned job with ID: {}", result.getId());
            return result;
        } catch (Exception e) {
            logger.error("Failed to assign job", e);
            throw e;
        }
    }

    @PreAuthorize("hasRole('LAUNDRY_WORKER') or hasRole('ADMIN')")
    @GetMapping("/worker/{workerId}")
    public List<LaundryJobDto> getByWorker(@PathVariable Long workerId) {
        logger.info("Fetching jobs for workerId={}", workerId);
        try {
            List<LaundryJobDto> jobs = laundryJobService.getJobsByWorkerId(workerId);
            logger.info("Found {} job(s) for workerId={}", jobs.size(), workerId);
            return jobs;
        } catch (Exception e) {
            logger.error("Failed to fetch jobs for workerId={}", workerId, e);
            throw e;
        }
    }

    @PreAuthorize("hasRole('LAUNDRY_WORKER') or hasRole('ADMIN')")
    @PutMapping("/{jobId}/paid")
    public LaundryJobDto updatePaidStatus(@PathVariable Long jobId, @Valid @RequestParam boolean paid) {
        logger.info("Updating paid status for jobId={} to {}", jobId, paid);
        try {
            LaundryJobDto updated = laundryJobService.updateJobStatus(jobId, paid);
            logger.info("Updated paid status for jobId={} successfully", jobId);
            return updated;
        } catch (Exception e) {
            logger.error("Failed to update paid status for jobId={}", jobId, e);
            throw e;
        }
    }
}
