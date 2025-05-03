package com.mehdi.twosidedplatform.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "laundry_jobs")
public class LaundryJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "request_id", nullable = false)
    private LaundryRequest request;

    @ManyToOne
    @JoinColumn(name = "worker_id", nullable = false)
    private User worker;

    private LocalDateTime pickupTime;

    private LocalDateTime dropoffTime;

    private boolean paid;

    public Long getId() {
        return id;
    }

    public LaundryRequest getRequest() {
        return request;
    }

    public void setRequest(LaundryRequest request) {
        this.request = request;
    }

    public User getWorker() {
        return worker;
    }

    public void setWorker(User worker) {
        this.worker = worker;
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
