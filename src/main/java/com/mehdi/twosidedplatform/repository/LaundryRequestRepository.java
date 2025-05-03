package com.mehdi.twosidedplatform.repository;

import com.mehdi.twosidedplatform.entity.LaundryRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LaundryRequestRepository extends JpaRepository<LaundryRequest, Long> {
}
