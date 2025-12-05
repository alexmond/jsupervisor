package org.alexmond.jsupervisor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.SupervisorConfig;
import org.alexmond.jsupervisor.model.SupervisorInfo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for supervisor system information.
 * Provides endpoints for system monitoring and supervisor status.
 */
@RestController
@RequestMapping("/api/v1/supervisor")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Supervisor Information", description = "APIs for supervisor system information and monitoring")
@ConditionalOnProperty(prefix = "jsupervisor.rest", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SupervisorController {

    private final SupervisorConfig supervisorConfig;

    @GetMapping("/info")
    @Operation(summary = "Get supervisor info", description = "Retrieve basic supervisor information")
    public SupervisorInfo getSupervisorInfo() {
        return new SupervisorInfo(supervisorConfig);
    }
}
