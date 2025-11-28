package com.logistics.optimizer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VehicleInputDto(
        @NotBlank String id,
        @NotNull Double capacityKg,
        Double speedKmph,
        Double costPerKm,
        Double costPerHour,
        String vehicleType
) {
}

