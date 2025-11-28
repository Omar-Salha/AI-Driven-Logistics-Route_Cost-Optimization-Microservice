package com.logistics.optimizer.repository;

import com.logistics.optimizer.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RouteRepository extends JpaRepository<Route, UUID> {
    List<Route> findByOptimizationRunId(UUID optimizationRunId);
}

