package com.logistics.optimizer.service;

import com.logistics.optimizer.dto.CostEstimateRequest;
import com.logistics.optimizer.dto.CostEstimateResponse;
import com.logistics.optimizer.dto.StopRequestDto;
import com.logistics.optimizer.util.GeoUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@Service
public class CostEstimationService {

    private static final double DEFAULT_SPEED_KMPH = 48.0;
    private static final double DEFAULT_COST_PER_KM = 0.7;

    @Cacheable("cost-estimates")
    public CostEstimateResponse estimate(CostEstimateRequest request) {
        List<StopRequestDto> orderedStops = request.stops().stream()
                .sorted(Comparator.comparing(StopRequestDto::id))
                .toList();

        double distance = 0;
        double prevLat = request.depot().lat();
        double prevLng = request.depot().lng();
        for (StopRequestDto stop : orderedStops) {
            distance += GeoUtils.distanceKm(prevLat, prevLng, stop.lat(), stop.lng());
            prevLat = stop.lat();
            prevLng = stop.lng();
        }
        distance += GeoUtils.distanceKm(prevLat, prevLng, request.depot().lat(), request.depot().lng());

        double speed = DEFAULT_SPEED_KMPH;
        int durationMinutes = (int) Math.round(distance / speed * 60);
        double cost = distance * DEFAULT_COST_PER_KM;
        Instant eta = Instant.now().plus(durationMinutes, ChronoUnit.MINUTES);

        return new CostEstimateResponse(round(distance), durationMinutes, round(cost), eta, "heuristic");
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}

