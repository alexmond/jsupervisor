package org.alexmond.jsupervisor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.ProcessConfig;
import org.alexmond.jsupervisor.config.SupervisorConfig;
import org.alexmond.jsupervisor.repository.EventRepository;
import org.alexmond.jsupervisor.repository.ProcessRepository;
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
@Component
@Order(100) // Execute after most other application listeners
@RequiredArgsConstructor
public class JSupervisorStartupManager implements ApplicationListener<ApplicationReadyEvent> {

    private final SupervisorConfig supervisorConfig;
    private final ProcessManager processManager;
    private final ProcessManagerBulk processManagerBulk;
    private final ProcessRepository processRepository;
    private final EventRepository eventRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("╔═══════════════════════════════════════════════════════════╗");
        log.info("║         JSupervisor Starting Up                          ║");
        log.info("╚═══════════════════════════════════════════════════════════╝");

        try {
            // Step 1: Validate configuration
            validateConfiguration();

            // Step 2: Initialize directories
            initializeDirectories();

            // Step 3: Display supervisor information
            displaySupervisorInfo();

            // Step 4: Display process configuration
            displayProcessConfiguration();

            // Step 5: Auto-start processes if enabled
            handleAutoStart();

            // Step 6: Display final status
            displayStartupSummary();

        } catch (Exception e) {
            log.error("Failed to initialize JSupervisor", e);
            // Optionally: System.exit(1) if critical failure
        }

        log.info("╔═══════════════════════════════════════════════════════════╗");
        log.info("║         JSupervisor Ready                                 ║");
        log.info("╚═══════════════════════════════════════════════════════════╝");
    }

    /**
     * Validates the supervisor configuration
     */
    private void validateConfiguration() {
        log.info("Validating configuration...");

        if (supervisorConfig.getProcess().isEmpty()) {
            log.warn("No processes configured!");
            return;
        }

        int validProcesses = 0;
        int invalidProcesses = 0;

        for (Map.Entry<String, ProcessConfig> entry : supervisorConfig.getProcess().entrySet()) {
            String name = entry.getKey();
            ProcessConfig config = entry.getValue();

            if (validateProcessConfig(name, config)) {
                validProcesses++;
            } else {
                invalidProcesses++;
            }
        }

        log.info("Configuration validation complete: {} valid, {} invalid", 
                 validProcesses, invalidProcesses);

        if (invalidProcesses > 0) {
            log.warn("Some processes have invalid configuration and may fail to start");
        }
    }

    /**
     * Validates a single process configuration
     */
    private boolean validateProcessConfig(String name, ProcessConfig config) {
        List<String> errors = new ArrayList<>();

        // Check command
        if (config.getCommand() == null || config.getCommand().isEmpty()) {
            errors.add("Command is required");
        }

        // Check working directory
        if (config.getWorkingDirectory() != null) {
            File workDir = new File(config.getWorkingDirectory());
            if (!workDir.exists()) {
                errors.add("Working directory does not exist: " + config.getWorkingDirectory());
            } else if (!workDir.isDirectory()) {
                errors.add("Working directory is not a directory: " + config.getWorkingDirectory());
            }
        }

        // Check log file directories
        validateLogFileDirectory(config.getStdoutLogfile(), errors);
        validateLogFileDirectory(config.getStderrLogfile(), errors);

        if (!errors.isEmpty()) {
            log.error("Process '{}' has configuration errors:", name);
            errors.forEach(error -> log.error("  - {}", error));
            return false;
        }

        log.debug("Process '{}' configuration is valid", name);
        return true;
    }

    /**
     * Validates that the directory for a log file exists
     */
    private void validateLogFileDirectory(String logFile, List<String> errors) {
        if (logFile != null && !logFile.isEmpty()) {
            File file = new File(logFile);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                errors.add("Log file directory does not exist: " + parentDir.getAbsolutePath());
            }
        }
    }

    /**
     * Initializes required directories
     */
    private void initializeDirectories() {
        log.info("Initializing directories...");

        // Create log directory if specified
        if (supervisorConfig.getLogDirectory() != null) {
            createDirectory(supervisorConfig.getLogDirectory());
        }

        // Create PID directory if specified
        if (supervisorConfig.getPidDirectory() != null) {
            createDirectory(supervisorConfig.getPidDirectory());
        }

        // Create process log directories
        for (Map.Entry<String, ProcessConfig> entry : supervisorConfig.getProcess().entrySet()) {
            ProcessConfig config = entry.getValue();
            createLogDirectories(config);
        }
    }

    /**
     * Creates log directories for a process
     */
    private void createLogDirectories(ProcessConfig config) {
        if (config.getStdoutLogfile() != null) {
            createParentDirectory(config.getStdoutLogfile());
        }
        if (config.getStderrLogfile() != null) {
            createParentDirectory(config.getStderrLogfile());
        }
        if (config.getApplicationLog() != null) {
            createParentDirectory(config.getApplicationLog());
        }
    }

    /**
     * Creates parent directory for a file
     */
    private void createParentDirectory(String filePath) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null) {
            createDirectory(parentDir.getAbsolutePath());
        }
    }

    /**
     * Creates a directory if it doesn't exist
     */
    private void createDirectory(String dirPath) {
        try {
            Path path = Paths.get(dirPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                log.debug("Created directory: {}", dirPath);
            }
        } catch (IOException e) {
            log.error("Failed to create directory: {}", dirPath, e);
        }
    }

    /**
     * Displays supervisor information
     */
    private void displaySupervisorInfo() {
        log.info("┌─────────────────────────────────────────────────────────┐");
        log.info("│ Supervisor Information                                  │");
        log.info("├─────────────────────────────────────────────────────────┤");
        log.info("│ Node Name:    {}", String.format("%-42s", supervisorConfig.getNodeeName()));
        log.info("│ Description:  {}", String.format("%-42s", supervisorConfig.getDescription()));
        log.info("│ Enabled:      {}", String.format("%-42s", supervisorConfig.isEnabled()));
        log.info("│ Auto-start:   {}", String.format("%-42s", supervisorConfig.isAutostart()));
        log.info("│ Processes:    {}", String.format("%-42s", supervisorConfig.getProcess().size()));
        log.info("└─────────────────────────────────────────────────────────┘");
    }

    /**
     * Displays process configuration summary
     */
    private void displayProcessConfiguration() {
        log.info("Configured Processes:");
        log.info("┌──────────────────┬─────────────┬──────────────────────────┐");
        log.info("│ Process Name     │ Auto-start  │ Command                  │");
        log.info("├──────────────────┼─────────────┼──────────────────────────┤");

        List<Map.Entry<String, ProcessConfig>> sortedProcesses = supervisorConfig.getProcess().entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getValue().getPriority() != null ? 
                        e.getValue().getPriority() : Integer.MAX_VALUE))
                .toList();

        for (Map.Entry<String, ProcessConfig> entry : sortedProcesses) {
            String name = entry.getKey();
            ProcessConfig config = entry.getValue();
            boolean autostart = config.isAutostart() != null ? config.isAutostart() : 
                               supervisorConfig.isAutostart();
            String command = config.getCommand();
            if (command.length() > 24) {
                command = command.substring(0, 21) + "...";
            }

            log.info("│ {:<16} │ {:<11} │ {:<24} │",
                    truncate(name, 16),
                    autostart ? "Yes" : "No",
                    command);
        }
        log.info("└──────────────────┴─────────────┴──────────────────────────┘");
    }

    /**
     * Handles auto-starting of processes
     */
    private void handleAutoStart() {
        if (!supervisorConfig.isAutostart()) {
            log.info("Global auto-start is disabled");
            return;
        }

        log.info("Starting processes with auto-start enabled...");

        // Count processes to auto-start
        long autoStartCount = supervisorConfig.getProcess().entrySet().stream()
                .filter(e -> {
                    Boolean processAutostart = e.getValue().isAutostart();
                    return processAutostart == null || processAutostart;
                })
                .count();

        if (autoStartCount == 0) {
            log.info("No processes configured for auto-start");
            return;
        }

        try {
            // Start all processes (respecting individual autostart settings)
            startProcessesInPriorityOrder();
            log.info("Auto-start initiated for {} process(es)", autoStartCount);
        } catch (Exception e) {
            log.error("Failed to auto-start processes", e);
        }
    }

    /**
     * Starts processes in priority order
     */
    private void startProcessesInPriorityOrder() {
        List<Map.Entry<String, ProcessConfig>> sortedProcesses = supervisorConfig.getProcess().entrySet().stream()
                .filter(e -> {
                    Boolean autostart = e.getValue().isAutostart();
                    return autostart == null || autostart; // Auto-start if not specified or true
                })
                .sorted(Comparator.comparing(e -> e.getValue().getPriority() != null ? 
                        e.getValue().getPriority() : Integer.MAX_VALUE))
                .toList();

        for (Map.Entry<String, ProcessConfig> entry : sortedProcesses) {
            String name = entry.getKey();
            try {
                log.info("Starting process: {}", name);
                processManager.startProcess(name);
                
                // Small delay between starts if priority-based
                if (entry.getValue().getPriority() != null) {
                    Thread.sleep(100);
                }
            } catch (Exception e) {
                log.error("Failed to start process: {}", name, e);
            }
        }
    }

    /**
     * Displays startup summary
     */
    private void displayStartupSummary() {
        // Wait a moment for initial status updates
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log.info("Startup Summary:");
        log.info("┌──────────────────┬─────────────────┐");
        log.info("│ Process Name     │ Status          │");
        log.info("├──────────────────┼─────────────────┤");

        processRepository.findAll().forEach((name, process) -> {
            String status = process.getProcessStatus().name();
            log.info("│ {:<16} │ {:<15} │", truncate(name, 16), truncate(status, 15));
        });

        log.info("└──────────────────┴─────────────────┘");
        log.info("Total events logged: {}", eventRepository.findAll().size());
    }

    /**
     * Truncates a string to specified length
     */
    private String truncate(String str, int length) {
        if (str == null) return "";
        if (str.length() <= length) return str;
        return str.substring(0, length - 3) + "...";
    }
}
