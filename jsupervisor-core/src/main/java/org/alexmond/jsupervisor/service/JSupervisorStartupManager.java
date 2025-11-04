package org.alexmond.jsupervisor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.ProcessConfig;
import org.alexmond.jsupervisor.config.SupervisorConfig;
import org.alexmond.jsupervisor.repository.EventRepository;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.alexmond.jsupervisor.utility.ValidateConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Comprehensive startup manager for JSupervisor.
 * Handles initialization, validation, and auto-start of processes.
 * Uses ApplicationListener for more control over execution order.
 */
@Slf4j
@Order(100) // Execute after most other application listeners
@RequiredArgsConstructor
public class JSupervisorStartupManager implements ApplicationListener<ApplicationReadyEvent> {
//
    private final SupervisorConfig supervisorConfig;
    private final ProcessManagerBulk processManagerBulk;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ValidateConfiguration validateConfiguration = new ValidateConfiguration(supervisorConfig);
        log.info("JSupervisor Starting Up");

        try {
            validateConfiguration.validate();

//            initializeDirectories();
//
            handleAutoStart();

        } catch (Exception e) {
            log.error("Failed to initialize JSupervisor", e);
            // Optionally: System.exit(1) if critical failure
        }

        log.info("JSupervisor Ready");
    }

    /**
     * Handles auto-starting of processes
     */
    private void handleAutoStart() {

        if (!supervisorConfig.isAutoStart()) {
            log.info("Global auto-start is disabled");
            return;
        }

        log.info("Starting processes with auto-start enabled...");
        processManagerBulk.autoStartAll();
    }

}
