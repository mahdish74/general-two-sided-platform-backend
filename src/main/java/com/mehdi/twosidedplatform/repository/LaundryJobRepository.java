package com.mehdi.twosidedplatform.repository;

import com.mehdi.twosidedplatform.entity.LaundryJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LaundryJobRepository extends JpaRepository<LaundryJob, Long> {
}
