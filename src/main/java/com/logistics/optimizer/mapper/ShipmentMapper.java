package com.logistics.optimizer.mapper;

import com.logistics.optimizer.dto.ShipmentResponse;
import com.logistics.optimizer.dto.StopDto;
import com.logistics.optimizer.entity.Shipment;
import com.logistics.optimizer.entity.Stop;

import java.util.Comparator;
import java.util.List;

public final class ShipmentMapper {

    private ShipmentMapper() {
    }

    public static ShipmentResponse toDto(Shipment shipment) {
        List<StopDto> stops = shipment.getStops().stream()
                .sorted(Comparator.comparing(stop -> stop.getSequenceOrder() == null ? Integer.MAX_VALUE : stop.getSequenceOrder()))
                .map(ShipmentMapper::toDto)
                .toList();

        return new ShipmentResponse(
                shipment.getId(),
                shipment.getExternalRef(),
                shipment.getStatus().name(),
                shipment.getCreatedAt(),
                stops
        );
    }

    private static StopDto toDto(Stop stop) {
        return new StopDto(
                stop.getId(),
                stop.getSequenceOrder(),
                stop.getType() != null ? stop.getType().name().toLowerCase() : null,
                stop.getLatitude(),
                stop.getLongitude(),
                stop.getWeightKg(),
                stop.getServiceMinutes(),
                stop.getEarliestArrival(),
                stop.getLatestArrival(),
                stop.getAddress()
        );
    }
}

