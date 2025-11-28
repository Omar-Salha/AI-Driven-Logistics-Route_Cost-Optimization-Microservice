package com.logistics.optimizer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.logistics.optimizer.dto.OptimizeRequest;
import com.logistics.optimizer.dto.OptimizeResponse;
import com.logistics.optimizer.entity.OptimizationRun;
import com.logistics.optimizer.entity.OptimizationStatus;
import com.logistics.optimizer.repository.OptimizationRunRepository;
import com.logistics.optimizer.repository.RouteRepository;
import com.logistics.optimizer.service.model.OptimizationComputation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class OptimizationService {

    private static final Logger log = LoggerFactory.getLogger(OptimizationService.class);
    private static final int MAX_SYNC_STOPS = 40;

    private final OptimizationRunRepository optimizationRunRepository;
    private final RouteRepository routeRepository;
    private final HeuristicPlanner heuristicPlanner;
    private final ObjectMapper objectMapper;
    private final TaskExecutor optimizationExecutor;

    public OptimizationService(OptimizationRunRepository optimizationRunRepository,
                               RouteRepository routeRepository,
                               HeuristicPlanner heuristicPlanner,
                               ObjectMapper objectMapper,
                               TaskExecutor optimizationExecutor) {
        this.optimizationRunRepository = optimizationRunRepository;
        this.routeRepository = routeRepository;
        this.heuristicPlanner = heuristicPlanner;
        this.objectMapper = objectMapper;
        this.optimizationExecutor = optimizationExecutor;
    }

    @Transactional
    public OptimizeResponse optimize(OptimizeRequest request) {
        OptimizationRun run = new OptimizationRun();
        run.setStatus(OptimizationStatus.PENDING);
        run.setRequestPayload(writePayload(request));
        run.setStartedAt(Instant.now());
        run = optimizationRunRepository.save(run);

        if (shouldRunAsync(request)) {
            UUID runId = run.getId();
            optimizationExecutor.execute(() -> executeAsync(runId, request));
            return new OptimizeResponse(runId, OptimizationStatus.PENDING.name(), java.util.List.of(), null, "async", "Optimization scheduled");
        }

        OptimizationComputation computation = heuristicPlanner.plan(request);
        return finalizeRun(run, computation, "sync");
    }

    @Transactional(readOnly = true)
    public OptimizationRun getOptimizationRun(UUID id) {
        return optimizationRunRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Optimization run " + id + " not found"));
    }

    private OptimizeResponse finalizeRun(OptimizationRun run, OptimizationComputation computation, String mode) {
        Instant completedAt = Instant.now();
        run.setCompletedAt(completedAt);
        run.setDurationMs(completedAt.toEpochMilli() - run.getStartedAt().toEpochMilli());
        run.setStatus(OptimizationStatus.COMPLETED);
        run.setResultPayload(writePayload(computation));
        optimizationRunRepository.save(run);

        routeRepository.deleteAll(routeRepository.findByOptimizationRunId(run.getId()));
        computation.routes().forEach(dto -> {
            var route = new com.logistics.optimizer.entity.Route();
            route.setOptimizationRun(run);
            route.setSequence(writePayload(dto.sequence()));
            route.setDistanceKm(dto.distanceKm());
            route.setDurationMinutes(dto.durationMinutes());
            route.setCost(dto.cost());
            routeRepository.save(route);
        });
        return new OptimizeResponse(run.getId(), run.getStatus().name(), computation.routes(), computation.metrics(), mode, null);
    }

    private void executeAsync(UUID runId, OptimizeRequest request) {
        try {
            OptimizationComputation computation = heuristicPlanner.plan(request);
            optimizationRunRepository.findById(runId).ifPresent(run -> finalizeRun(run, computation, "async"));
        } catch (Exception ex) {
            log.error("Async optimization failed for {}", runId, ex);
            markFailed(runId, ex);
        }
    }

    @Transactional
    public void markFailed(UUID runId, Throwable throwable) {
        optimizationRunRepository.findById(runId).ifPresent(run -> {
            run.setStatus(OptimizationStatus.FAILED);
            run.setCompletedAt(Instant.now());
            run.setResultPayload("{\"error\":\"" + throwable.getMessage() + "\"}");
            optimizationRunRepository.save(run);
        });
    }

    private boolean shouldRunAsync(OptimizeRequest request) {
        return (request.stops().size() > MAX_SYNC_STOPS) || Boolean.TRUE.equals(request.asyncHint());
    }

    private String writePayload(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize payload", e);
            return "{}";
        }
    }
}

