package com.logistics.optimizer.dto;

import java.util.List;
import java.util.UUID;

public record OptimizeResponse(
        UUID optimizationRunId,
        String status,
        List<RoutePlanDto> routes,
        OptimizationMetricsDto metrics,
        String mode,
        String warning
) {
}

