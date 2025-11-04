
package org.alexmond.jsupervisor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.repository.ProcessRepository;
import org.alexmond.jsupervisor.repository.RunningProcess;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for process log management.
 * Provides endpoints for accessing and managing process logs.
 */
@RestController
@RequestMapping("/api/v1/logs")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Log Management", description = "APIs for accessing process logs")
@ConditionalOnProperty(prefix = "jsupervisor.rest", name = "enabled", havingValue = "true", matchIfMissing = true)
public class LogController {

    private final ProcessRepository processRepository;

    @GetMapping("/{processName}/stdout")
    @Operation(summary = "Get stdout log", description = "Retrieve stdout log content for a specific process")
    public ResponseEntity<Map<String, Object>> getStdoutLog(
            @Parameter(description = "Process name") @PathVariable String processName,
            @Parameter(description = "Number of lines to return from end of file") @RequestParam(defaultValue = "100") int lines,
            @Parameter(description = "Follow log (return streaming response)") @RequestParam(defaultValue = "false") boolean follow) {
        
        RunningProcess runningProcess = processRepository.getRunningProcess(processName);
        if (runningProcess == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            File stdoutFile = runningProcess.getStdout();
            if (stdoutFile == null || !stdoutFile.exists()) {
                return ResponseEntity.ok(Map.of(
                    "processName", processName,
                    "logType", "stdout",
                    "content", "",
                    "lines", 0,
                    "file", "N/A"
                ));
            }

            List<String> logLines = Files.readAllLines(stdoutFile.toPath());
            List<String> tailLines = logLines.size() > lines ? 
                logLines.subList(Math.max(0, logLines.size() - lines), logLines.size()) : logLines;

            return ResponseEntity.ok(Map.of(
                "processName", processName,
                "logType", "stdout",
                "content", String.join("\n", tailLines),
                "lines", tailLines.size(),
                "totalLines", logLines.size(),
                "file", stdoutFile.getName(),
                "lastModified", stdoutFile.lastModified()
            ));
        } catch (IOException e) {
            log.error("Failed to read stdout log for process: {}", processName, e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to read log: " + e.getMessage()));
        }
    }

    @GetMapping("/{processName}/stderr")
    @Operation(summary = "Get stderr log", description = "Retrieve stderr log content for a specific process")
    public ResponseEntity<Map<String, Object>> getStderrLog(
            @Parameter(description = "Process name") @PathVariable String processName,
            @Parameter(description = "Number of lines to return from end of file") @RequestParam(defaultValue = "100") int lines) {
        
        RunningProcess runningProcess = processRepository.getRunningProcess(processName);
        if (runningProcess == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            File stderrFile = runningProcess.getStderr();
            if (stderrFile == null || !stderrFile.exists()) {
                return ResponseEntity.ok(Map.of(
                    "processName", processName,
                    "logType", "stderr",
                    "content", "",
                    "lines", 0,
                    "file", "N/A"
                ));
            }

            List<String> logLines = Files.readAllLines(stderrFile.toPath());
            List<String> tailLines = logLines.size() > lines ? 
                logLines.subList(Math.max(0, logLines.size() - lines), logLines.size()) : logLines;

            return ResponseEntity.ok(Map.of(
                "processName", processName,
                "logType", "stderr",
                "content", String.join("\n", tailLines),
                "lines", tailLines.size(),
                "totalLines", logLines.size(),
                "file", stderrFile.getName(),
                "lastModified", stderrFile.lastModified()
            ));
        } catch (IOException e) {
            log.error("Failed to read stderr log for process: {}", processName, e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to read log: " + e.getMessage()));
        }
    }

    @GetMapping("/{processName}/logs")
    @Operation(summary = "List log files", description = "Get information about all log files for a process")
    public ResponseEntity<Map<String, Object>> getLogFiles(
            @Parameter(description = "Process name") @PathVariable String processName) {
        
        RunningProcess runningProcess = processRepository.getRunningProcess(processName);
        if (runningProcess == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> logInfo = Map.of(
            "processName", processName,
            "stdoutLogfile", runningProcess.getStdoutLogfile(),
            "stderrLogfile", runningProcess.getStderrLogfile(),
            "stdoutExists", runningProcess.getStdout() != null && runningProcess.getStdout().exists(),
            "stderrExists", runningProcess.getStderr() != null && runningProcess.getStderr().exists(),
            "stdoutSize", runningProcess.getStdout() != null && runningProcess.getStdout().exists() ? runningProcess.getStdout().length() : 0,
            "stderrSize", runningProcess.getStderr() != null && runningProcess.getStderr().exists() ? runningProcess.getStderr().length() : 0
        );

        return ResponseEntity.ok(logInfo);
    }
}
