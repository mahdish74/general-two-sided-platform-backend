package com.mehdi.twosidedplatform.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class LaundryJobDto {

    private Long id;

    @NotNull(message = "Request ID is required")
    private Long requestId;

    @NotNull(message = "Worker ID is required")
    private Long workerId;

    @NotNull(message = "Pickup time is required")
    @Future(message = "Pickup time must be in the future")
    private LocalDateTime pickupTime;

    @NotNull(message = "Dropoff time is required")
    @Future(message = "Dropoff time must be in the future")
    private LocalDateTime dropoffTime;

    private boolean paid;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public LocalDateTime getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(LocalDateTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public LocalDateTime getDropoffTime() {
        return dropoffTime;
    }

    public void setDropoffTime(LocalDateTime dropoffTime) {
        this.dropoffTime = dropoffTime;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}
