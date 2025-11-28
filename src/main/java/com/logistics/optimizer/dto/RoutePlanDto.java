package com.logistics.optimizer.dto;

import java.time.Instant;
import java.util.List;

public record RoutePlanDto(
        String routeId,
        String vehicleId,
        List<String> sequence,
        Double distanceKm,
        Integer durationMinutes,
        Double cost,
        Instant eta
) {
}

