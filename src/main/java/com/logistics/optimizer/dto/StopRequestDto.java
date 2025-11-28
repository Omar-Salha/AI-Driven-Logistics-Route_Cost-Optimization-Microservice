package com.logistics.optimizer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record StopRequestDto(
        @NotBlank String id,
        @NotBlank String type,
        @NotNull Double lat,
        @NotNull Double lng,
        Double weightKg,
        Integer serviceMinutes,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssX", timezone = "UTC")
        Instant earliestArrival,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssX", timezone = "UTC")
        Instant latestArrival
) {
}

