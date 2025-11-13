package org.alexmond.jsupervisor.utility;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.ProcessConfig;
import org.alexmond.jsupervisor.config.SupervisorConfig;

import java.util.Map;

/**
 * Utility class responsible for creating and managing directories required by the supervisor.
 * This includes log directories for process stdout and stderr outputs.
 */
@RequiredArgsConstructor
@Slf4j
public class CreateDirectories {

    /**
     * Configuration object containing supervisor settings and process configurations.
     */
    private final SupervisorConfig supervisorConfig;

    /**
     * Initializes required directories for all configured processes.
     * Creates necessary log directories based on process configurations
     * defined in the supervisor config.
     */
    private void initializeDirectories() {
        log.info("Initializing directories...");

        // Create process log directories
        for (Map.Entry<String, ProcessConfig> entry : supervisorConfig.getProcess().entrySet()) {
            ProcessConfig config = entry.getValue();
//            createLogDirectories(config);
        }
    }

    /**
     * Creates log directories for a process
     */
//    private void createLogDirectories(ProcessConfig config) {
//        if (config.getStdoutLogfile() != null) {
//            createParentDirectory(config.getStdoutLogfile());
//        }
//        if (config.getStderrLogfile() != null) {
//            createParentDirectory(config.getStderrLogfile());
//        }
//    }
//
//    /**
//     * Creates parent directory for a file
//     */
//    private void createParentDirectory(String filePath) {
//        File file = new File(filePath);
//        File parentDir = file.getParentFile();
//        if (parentDir != null) {
//            createDirectory(parentDir.getAbsolutePath());
//        }
//    }
//
//    /**
//     * Creates a directory if it doesn't exist
//     */
//    private void createDirectory(String dirPath) {
//        try {
//            Path path = Paths.get(dirPath);
//            if (!Files.exists(path)) {
//                Files.createDirectories(path);
//                log.debug("Created directory: {}", dirPath);
//            }
//        } catch (IOException e) {
//            log.error("Failed to create directory: {}", dirPath, e);
//        }
//    }

}
