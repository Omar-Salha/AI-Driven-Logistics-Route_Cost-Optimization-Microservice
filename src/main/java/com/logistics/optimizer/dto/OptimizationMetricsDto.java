package com.logistics.optimizer.dto;

public record OptimizationMetricsDto(
        Double totalDistanceKm,
        Integer totalDurationMinutes,
        Double totalCost
) {
}

