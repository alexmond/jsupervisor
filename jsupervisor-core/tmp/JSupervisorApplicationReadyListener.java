package org.alexmond.jsupervisor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.SupervisorConfig;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Listener that handles application startup completion.
 * Executes initialization tasks when the Spring Boot application is fully started and ready.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JSupervisorApplicationReadyListener {

    private final SupervisorConfig supervisorConfig;
    private final ProcessManagerBulk processManagerBulk;
    private final ProcessRepository processRepository;

    /**
     * Handles the ApplicationReadyEvent.
     * This method is called when the application is fully started and ready to serve requests.
     *
     * @param event the ApplicationReadyEvent
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady(ApplicationReadyEvent event) {
        log.info("JSupervisor application is ready. Node: {}", supervisorConfig.getNodeName());

        // Log supervisor information
        logSupervisorInfo();

        // Auto-start configured processes
        autoStartProcesses();

        // Log process status
        logProcessStatus();

        log.info("JSupervisor initialization completed");
    }

    /**
     * Logs supervisor configuration information
     */
    private void logSupervisorInfo() {
        log.info("=".repeat(60));
        log.info("JSupervisor Node: {}", supervisorConfig.getNodeName());
        log.info("Description: {}", supervisorConfig.getDescription());
        log.info("Total configured processes: {}", supervisorConfig.getProcess().size());
        log.info("=".repeat(60));
    }

    /**
     * Auto-starts processes based on configuration
     */
    private void autoStartProcesses() {
        if (supervisorConfig.isAutotart()) {
            log.info("Auto-starting all configured processes...");
            try {
                processManagerBulk.startAll();
                log.info("All processes scheduled for startup");
            } catch (IOException e) {
                log.error("Failed to auto-start processes", e);
            }
        } else {
            log.info("Auto-start is disabled. Processes must be started manually.");
        }
    }

    /**
     * Logs current status of all processes
     */
    private void logProcessStatus() {
        log.info("Process Status Summary:");
        processRepository.findAll().forEach((name, process) -> {
            log.info("  - {} [{}]", name, process.getProcessStatus());
        });
    }
}
