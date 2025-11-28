package com.logistics.optimizer.dto;

import java.time.Instant;
import java.util.UUID;

public record StopDto(
        UUID id,
        Integer sequenceOrder,
        String type,
        Double lat,
        Double lng,
        Double weightKg,
        Integer serviceMinutes,
        Instant earliestArrival,
        Instant latestArrival,
        String address
) {
}

