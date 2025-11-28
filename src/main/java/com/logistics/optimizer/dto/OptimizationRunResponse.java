package com.logistics.optimizer.dto;

import java.time.Instant;
import java.util.UUID;

public record OptimizationRunResponse(
        UUID id,
        String status,
        Instant startedAt,
        Instant completedAt,
        Long durationMs,
        String requestPayload,
        String resultPayload
) {
}

