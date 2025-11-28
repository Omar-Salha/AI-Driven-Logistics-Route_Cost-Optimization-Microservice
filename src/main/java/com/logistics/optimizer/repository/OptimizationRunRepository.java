package com.logistics.optimizer.repository;

import com.logistics.optimizer.entity.OptimizationRun;
import com.logistics.optimizer.entity.OptimizationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface OptimizationRunRepository extends JpaRepository<OptimizationRun, UUID> {

    List<OptimizationRun> findByStatusAndCreatedAtBefore(OptimizationStatus status, Instant createdBefore);
}

