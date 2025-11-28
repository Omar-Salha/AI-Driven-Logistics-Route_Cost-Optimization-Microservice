package com.logistics.optimizer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OptimizeRequest(
        @NotBlank String requestId,
        @NotEmpty List<@Valid CoordinateDto> depots,
        @NotEmpty List<@Valid VehicleInputDto> vehicles,
        @NotEmpty List<@Valid StopRequestDto> stops,
        @Valid ConstraintsDto constraints,
        Boolean asyncHint
) {
}

