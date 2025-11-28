package com.logistics.optimizer.dto;

public record ConstraintsDto(
        Integer maxRouteDurationMinutes,
        Boolean allowOverCapacity,
        Integer maxStopsPerVehicle
) {
}

