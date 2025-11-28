package com.logistics.optimizer.controller;

import com.logistics.optimizer.dto.RecomputeRequest;
import com.logistics.optimizer.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/recompute")
    public ResponseEntity<?> recompute(@Valid @RequestBody RecomputeRequest request) {
        return ResponseEntity.accepted().body(adminService.recompute(request));
    }
}

