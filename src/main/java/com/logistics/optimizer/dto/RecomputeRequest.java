package com.logistics.optimizer.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record RecomputeRequest(
        @NotEmpty List<UUID> shipmentIds
) {
}

