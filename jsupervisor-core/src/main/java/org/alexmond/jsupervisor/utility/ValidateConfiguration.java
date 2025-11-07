package org.alexmond.jsupervisor.utility;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.ProcessConfig;
import org.alexmond.jsupervisor.config.SupervisorConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class ValidateConfiguration {

    private final SupervisorConfig supervisorConfig;

    /**
     * Validates the supervisor configuration
     */
    public void validate() {
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
}
