package org.alexmond.jsupervisor.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.alexmond.jsupervisor.config.ProcessConfig;
import org.alexmond.jsupervisor.repository.RunningProcess;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * REST representation of a process status.
 * Contains detailed information about a running or completed process,
 * including its configuration, runtime statistics, and log file locations.
 * This class is used for transferring process status data between
 * the service layer and API consumers.
 */

@Data
@Schema(description = "Represents process status information")
public class ProcessStatusRest {

    /**
     * The name identifier of the process
     */
    @Schema(description = "Name of the process", example = "myapp")
    private String name;

    /**
     * Current execution status of the process
     */
    @Schema(description = "Current status of the process (e.g., running, stopped, failed)")
    private ProcessStatus status;

    /**
     * Process ID assigned by the operating system
     */
    @Schema(description = "Process ID assigned by the operating system", example = "1234")
    private Long pid;

    /**
     * Timestamp when the process was started
     */
    @Schema(description = "Timestamp when the process was started")
    private LocalDateTime startTime;

    /**
     * Timestamp when the process ended
     */
    @Schema(description = "Timestamp when the process ended")
    private LocalDateTime endTime;

    /**
     * Exit code returned by the process
     */
    @Schema(description = "Process exit code", example = "0")
    private Integer exitCode;

    /**
     * Duration for which the process has been running
     */
    @Schema(description = "Process runtime duration", format = "duration")
    private Duration processRuntime;

    /**
     * Human-readable formatted string of process uptime
     */
    @Schema(description = "Formatted process uptime", example = "2d 5h 30m")
    private String processUptime;

    /**
     * Configuration settings for the process
     */
    @Schema(description = "Process configuration settings")
    @JsonIgnore
    private ProcessConfig processConfig;

    /**
     * Path to the file where standard output is logged
     */
    @Schema(description = "File path where the standard output (stdout) of the process will be logged. If not specified, stdout will be inherited from the parent process.")
    private String stdoutLogfile;

    /**
     * Path to the file where standard error is logged
     */
    @Schema(description = "File path where the standard error (stderr) of the process will be logged. If not specified, stderr will be inherited from the parent process.")
    private String stderrLogfile;

    /**
     * Error log content captured when process failed to start
     */
    @Schema(description = "Error log content when process failed to start")
    private String failedErrorLog;

    /**
     * Constructs a ProcessStatusRest object from a running process.
     *
     * @param name           The name identifier of the process
     * @param runningProcess The running process instance containing detailed status information
     */
    public ProcessStatusRest(String name, RunningProcess runningProcess) {
        this.name = name;
        this.startTime = runningProcess.getStartTime();
        this.endTime = runningProcess.getEndTime();
        this.exitCode = runningProcess.getExitCode();
        processConfig = runningProcess.getProcessConfig();
        processRuntime = runningProcess.getProcessRuntime();
        processUptime = runningProcess.getProcessRuntimeFormatted();
        stdoutLogfile = runningProcess.getStdoutLogfile();
        stderrLogfile = runningProcess.getStderrLogfile();
        failedErrorLog = runningProcess.getFailedErrorLog();

        if (runningProcess.getProcess() != null) {
            pid = runningProcess.getProcess().pid();
        }
        status = runningProcess.getProcessStatus();
    }

    /**
     * Checks if the process is currently alive.
     *
     * @return true if the process has a valid PID, false otherwise
     */
    public boolean isAlive() {
        return pid != null;
    }

    /**
     * Converts this ProcessStatusRest object to a Map representation.
     * Configures date formatting and serialization options for the conversion.
     *
     * @return Map containing the process status information
     */
    public Map<String, Object> toMap() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configOverride(LocalDateTime.class)
                .setFormat(JsonFormat.Value.forPattern("yyyy-MM-dd'T'HH:mm"));
        return objectMapper.convertValue(this, Map.class);
    }
}
