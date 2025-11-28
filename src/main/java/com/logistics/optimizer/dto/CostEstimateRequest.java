package com.logistics.optimizer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CostEstimateRequest(
        @Valid CoordinateDto depot,
        @NotBlank String vehicleType,
        @NotNull List<@Valid StopRequestDto> stops
) {
}

