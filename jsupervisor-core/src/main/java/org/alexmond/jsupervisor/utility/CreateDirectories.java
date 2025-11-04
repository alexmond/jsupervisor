package org.alexmond.jsupervisor.utility;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.ProcessConfig;
import org.alexmond.jsupervisor.config.SupervisorConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class CreateDirectories {

    private final SupervisorConfig supervisorConfig;

    /**
     * Initializes required directories
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
