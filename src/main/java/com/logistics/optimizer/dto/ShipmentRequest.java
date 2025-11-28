package com.logistics.optimizer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ShipmentRequest(
        @NotBlank String externalRef,
        @NotEmpty List<@Valid StopCreateDto> stops
) {
}

