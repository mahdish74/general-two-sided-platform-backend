package com.mehdi.twosidedplatform.dto;

import com.mehdi.twosidedplatform.entity.enums.LaundryRequestStatus;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class LaundryRequestDto {

    private Long id;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "Address must not be blank")
    private String address;

    private String specialInstructions;

    @NotNull(message = "Preferred pickup time is required")
    @Future(message = "Pickup time must be in the future")
    private LocalDateTime preferredPickupTime;

    private LaundryRequestStatus status;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public LocalDateTime getPreferredPickupTime() {
        return preferredPickupTime;
    }

    public void setPreferredPickupTime(LocalDateTime preferredPickupTime) {
        this.preferredPickupTime = preferredPickupTime;
    }

    public LaundryRequestStatus getStatus() {
        return status;
    }

    public void setStatus(LaundryRequestStatus status) {
        this.status = status;
    }
}
