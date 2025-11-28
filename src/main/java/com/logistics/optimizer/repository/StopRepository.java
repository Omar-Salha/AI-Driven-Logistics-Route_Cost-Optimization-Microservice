package com.logistics.optimizer.repository;

import com.logistics.optimizer.entity.Stop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StopRepository extends JpaRepository<Stop, UUID> {
    List<Stop> findByShipmentId(UUID shipmentId);
}

