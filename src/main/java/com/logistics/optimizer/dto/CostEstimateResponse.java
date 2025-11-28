package com.logistics.optimizer.dto;

import java.time.Instant;

public record CostEstimateResponse(
        Double estimatedDistanceKm,
        Integer estimatedDurationMinutes,
        Double estimatedCost,
        Instant eta,
        String method
) {
}

