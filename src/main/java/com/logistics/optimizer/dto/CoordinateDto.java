package com.logistics.optimizer.dto;

import jakarta.validation.constraints.NotNull;

public record CoordinateDto(
        @NotNull Double lat,
        @NotNull Double lng
) {
}

