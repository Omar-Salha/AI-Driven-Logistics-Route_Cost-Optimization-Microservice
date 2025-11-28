package com.logistics.optimizer.service;

import com.logistics.optimizer.dto.CoordinateDto;
import com.logistics.optimizer.dto.ConstraintsDto;
import com.logistics.optimizer.dto.OptimizeRequest;
import com.logistics.optimizer.dto.StopRequestDto;
import com.logistics.optimizer.dto.VehicleInputDto;
import com.logistics.optimizer.service.model.OptimizationComputation;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HeuristicPlannerTest {

    private final HeuristicPlanner planner = new HeuristicPlanner();

    @Test
    void shouldBuildRoutesWithinCapacity() {
        OptimizeRequest request = new OptimizeRequest(
                "req-1",
                List.of(new CoordinateDto(25.2, 55.27)),
                List.of(new VehicleInputDto("veh-1", 2000.0, 60.0, 0.6, null, "van")),
                List.of(
                        new StopRequestDto("stop-1", "delivery", 25.21, 55.28, 200.0, 10,
                                Instant.now(), Instant.now().plusSeconds(3600)),
                        new StopRequestDto("stop-2", "delivery", 25.19, 55.25, 250.0, 10,
                                Instant.now(), Instant.now().plusSeconds(3600))
                ),
                new ConstraintsDto(480, false, null),
                false
        );

        OptimizationComputation computation = planner.plan(request);

        assertThat(computation.routes()).hasSize(1);
        assertThat(computation.routes().get(0).sequence()).containsExactly("stop-1", "stop-2");
        assertThat(computation.metrics().totalDistanceKm()).isGreaterThan(0);
        assertThat(computation.strategy()).isEqualTo("heuristic-greedy");
    }
}

