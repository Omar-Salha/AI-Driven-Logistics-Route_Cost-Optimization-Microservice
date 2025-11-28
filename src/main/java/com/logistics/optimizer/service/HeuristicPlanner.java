package com.logistics.optimizer.service;

import com.logistics.optimizer.dto.CoordinateDto;
import com.logistics.optimizer.dto.OptimizeRequest;
import com.logistics.optimizer.dto.RoutePlanDto;
import com.logistics.optimizer.dto.StopRequestDto;
import com.logistics.optimizer.dto.VehicleInputDto;
import com.logistics.optimizer.service.model.OptimizationComputation;
import com.logistics.optimizer.util.GeoUtils;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class HeuristicPlanner {

    private static final double DEFAULT_SPEED_KMPH = 45.0;
    private static final double DEFAULT_COST_PER_KM = 0.65;

    public OptimizationComputation plan(OptimizeRequest request) {
        CoordinateDto depot = request.depots().get(0);
        Map<String, List<StopRequestDto>> assignments = assignStops(request);

        List<RoutePlanDto> routes = new ArrayList<>();
        double totalDistance = 0;
        double totalCost = 0;
        int totalDuration = 0;

        for (Map.Entry<String, List<StopRequestDto>> entry : assignments.entrySet()) {
            if (entry.getValue().isEmpty()) {
                continue;
            }
            VehicleInputDto vehicle = request.vehicles().stream()
                    .filter(v -> v.id().equals(entry.getKey()))
                    .findFirst()
                    .orElseThrow();

            RouteComputation routeComputation = buildRoute(depot, vehicle, entry.getValue());
            totalDistance += routeComputation.distance;
            totalCost += routeComputation.cost;
            totalDuration += routeComputation.durationMinutes;
            routes.add(routeComputation.dto);
        }

        return new OptimizationComputation(
                routes,
                new com.logistics.optimizer.dto.OptimizationMetricsDto(totalDistance, totalDuration, totalCost),
                "heuristic-greedy"
        );
    }

    private Map<String, List<StopRequestDto>> assignStops(OptimizeRequest request) {
        Map<String, Double> remainingCapacity = new LinkedHashMap<>();
        Map<String, List<StopRequestDto>> assignments = new LinkedHashMap<>();
        for (VehicleInputDto vehicle : request.vehicles()) {
            remainingCapacity.put(vehicle.id(), vehicle.capacityKg() == null ? Double.MAX_VALUE : vehicle.capacityKg());
            assignments.put(vehicle.id(), new ArrayList<>());
        }

        List<StopRequestDto> sorted = new ArrayList<>(request.stops());
        sorted.sort(Comparator.comparingDouble(
                (StopRequestDto stop) -> stop.weightKg() == null ? 0 : stop.weightKg()
        ).reversed());

        boolean allowOverCapacity = request.constraints() != null && Boolean.TRUE.equals(request.constraints().allowOverCapacity());

        for (StopRequestDto stop : sorted) {
            String targetVehicle = findVehicleForStop(stop, remainingCapacity, allowOverCapacity);
            assignments.get(targetVehicle).add(stop);
            if (stop.weightKg() != null) {
                remainingCapacity.compute(targetVehicle, (key, capacity) -> capacity - stop.weightKg());
            }
        }
        return assignments;
    }

    private String findVehicleForStop(StopRequestDto stop, Map<String, Double> remainingCapacity, boolean allowOverCapacity) {
        for (Map.Entry<String, Double> entry : remainingCapacity.entrySet()) {
            if (entry.getValue() >= (stop.weightKg() == null ? 0 : stop.weightKg())) {
                return entry.getKey();
            }
        }
        if (allowOverCapacity) {
            return remainingCapacity.keySet().iterator().next();
        }
        return remainingCapacity.entrySet().stream()
                .min(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElseThrow();
    }

    private RouteComputation buildRoute(CoordinateDto depot, VehicleInputDto vehicle, List<StopRequestDto> stops) {
        List<StopRequestDto> ordered = nearestNeighbor(depot, stops);
        double distance = 0;
        double previousLat = depot.lat();
        double previousLng = depot.lng();
        List<String> sequence = new ArrayList<>();

        for (StopRequestDto stop : ordered) {
            distance += GeoUtils.distanceKm(previousLat, previousLng, stop.lat(), stop.lng());
            previousLat = stop.lat();
            previousLng = stop.lng();
            sequence.add(stop.id());
        }
        distance += GeoUtils.distanceKm(previousLat, previousLng, depot.lat(), depot.lng());

        double speed = vehicle.speedKmph() != null ? vehicle.speedKmph() : DEFAULT_SPEED_KMPH;
        int durationMinutes = (int) Math.round(distance / speed * 60);
        double costPerKm = vehicle.costPerKm() != null ? vehicle.costPerKm() : DEFAULT_COST_PER_KM;
        double cost = distance * costPerKm;
        Instant eta = Instant.now().plus(durationMinutes, ChronoUnit.MINUTES);

        RoutePlanDto dto = new RoutePlanDto(
                UUID.randomUUID().toString(),
                vehicle.id(),
                sequence,
                round(distance),
                durationMinutes,
                round(cost),
                eta
        );
        return new RouteComputation(dto, distance, durationMinutes, cost);
    }

    private List<StopRequestDto> nearestNeighbor(CoordinateDto depot, List<StopRequestDto> stops) {
        List<StopRequestDto> remaining = new ArrayList<>(stops);
        List<StopRequestDto> ordered = new ArrayList<>();
        double currentLat = depot.lat();
        double currentLng = depot.lng();
        while (!remaining.isEmpty()) {
            StopRequestDto closest = null;
            double minDistance = Double.MAX_VALUE;
            for (StopRequestDto candidate : remaining) {
                double dist = GeoUtils.distanceKm(currentLat, currentLng, candidate.lat(), candidate.lng());
                if (dist < minDistance) {
                    minDistance = dist;
                    closest = candidate;
                }
            }
            ordered.add(closest);
            remaining.remove(closest);
            currentLat = closest.lat();
            currentLng = closest.lng();
        }
        return ordered;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private record RouteComputation(RoutePlanDto dto, double distance, int durationMinutes, double cost) {
    }
}

