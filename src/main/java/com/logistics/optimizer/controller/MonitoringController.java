package com.logistics.optimizer.controller;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class MonitoringController {

    private final MeterRegistry meterRegistry;

    public MonitoringController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> summary() {
        var uptimeGauge = meterRegistry.find("process.uptime").gauge();
        double uptime = uptimeGauge != null ? uptimeGauge.value() : 0;
        return ResponseEntity.ok(Map.of(
                "status", "OK",
                "uptime_seconds", uptime,
                "available_metrics", meterRegistry.getMeters().size()
        ));
    }
}

