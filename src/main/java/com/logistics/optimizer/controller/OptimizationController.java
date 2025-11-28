package com.logistics.optimizer.controller;

import com.logistics.optimizer.dto.CostEstimateRequest;
import com.logistics.optimizer.dto.CostEstimateResponse;
import com.logistics.optimizer.dto.OptimizeRequest;
import com.logistics.optimizer.dto.OptimizeResponse;
import com.logistics.optimizer.dto.OptimizationRunResponse;
import com.logistics.optimizer.entity.OptimizationRun;
import com.logistics.optimizer.service.CostEstimationService;
import com.logistics.optimizer.service.OptimizationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class OptimizationController {

    private final OptimizationService optimizationService;
    private final CostEstimationService costEstimationService;

    public OptimizationController(OptimizationService optimizationService,
                                  CostEstimationService costEstimationService) {
        this.optimizationService = optimizationService;
        this.costEstimationService = costEstimationService;
    }

    @PostMapping("/optimize")
    public ResponseEntity<OptimizeResponse> optimize(@Valid @RequestBody OptimizeRequest request) {
        OptimizeResponse response = optimizationService.optimize(request);
        if ("async".equals(response.mode())) {
            return ResponseEntity.accepted().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/estimate-cost")
    public ResponseEntity<CostEstimateResponse> estimateCost(@Valid @RequestBody CostEstimateRequest request) {
        return ResponseEntity.ok(costEstimationService.estimate(request));
    }

    @GetMapping("/optimizations/{id}")
    public ResponseEntity<OptimizationRunResponse> getOptimization(@PathVariable UUID id) {
        OptimizationRun run = optimizationService.getOptimizationRun(id);
        OptimizationRunResponse response = new OptimizationRunResponse(
                run.getId(),
                run.getStatus().name(),
                run.getStartedAt(),
                run.getCompletedAt(),
                run.getDurationMs(),
                run.getRequestPayload(),
                run.getResultPayload()
        );
        return ResponseEntity.ok(response);
    }
}

