package com.mehdi.twosidedplatform.entity;

import com.mehdi.twosidedplatform.entity.enums.LaundryRequestStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "laundry_requests")
public class LaundryRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    private String address;

    private String specialInstructions;

    private LocalDateTime preferredPickupTime;

    @Enumerated(EnumType.STRING)
    private LaundryRequestStatus status;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
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
