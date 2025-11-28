package com.logistics.optimizer.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ShipmentResponse(
        UUID id,
        String externalRef,
        String status,
        Instant createdAt,
        List<StopDto> stops
) {
}

