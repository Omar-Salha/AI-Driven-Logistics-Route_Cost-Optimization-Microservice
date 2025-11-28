package com.logistics.optimizer.repository;

import com.logistics.optimizer.entity.Depot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DepotRepository extends JpaRepository<Depot, UUID> {
}

