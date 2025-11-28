package com.logistics.optimizer.service.model;

import com.logistics.optimizer.dto.OptimizationMetricsDto;
import com.logistics.optimizer.dto.RoutePlanDto;

import java.util.List;

public record OptimizationComputation(
        List<RoutePlanDto> routes,
        OptimizationMetricsDto metrics,
        String strategy
) {
}

