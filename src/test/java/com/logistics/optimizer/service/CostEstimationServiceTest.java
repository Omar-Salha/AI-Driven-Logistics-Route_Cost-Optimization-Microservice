package com.logistics.optimizer.service;

import com.logistics.optimizer.dto.CoordinateDto;
import com.logistics.optimizer.dto.CostEstimateRequest;
import com.logistics.optimizer.dto.CostEstimateResponse;
import com.logistics.optimizer.dto.StopRequestDto;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CostEstimationServiceTest {

    private final CostEstimationService service = new CostEstimationService();

    @Test
    void shouldEstimateCost() {
        CostEstimateRequest request = new CostEstimateRequest(
                new CoordinateDto(25.2048, 55.2708),
                "van",
                List.of(new StopRequestDto("s1", "delivery", 25.1972, 55.2744, 300.0, 10,
                        Instant.now(), Instant.now().plusSeconds(3600)))
        );

        CostEstimateResponse response = service.estimate(request);
        assertThat(response.estimatedDistanceKm()).isGreaterThan(0);
        assertThat(response.estimatedDurationMinutes()).isGreaterThan(0);
        assertThat(response.estimatedCost()).isGreaterThan(0);
    }
}

