package com.logistics.optimizer.service;

import com.logistics.optimizer.dto.RecomputeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminService.class);

    public Map<String, Object> recompute(RecomputeRequest request) {
        log.info("Admin triggered recompute for {}", request.shipmentIds());
        return Map.of(
                "status", "queued",
                "shipments", request.shipmentIds(),
                "message", "Recompute scheduled via admin endpoint"
        );
    }
}

