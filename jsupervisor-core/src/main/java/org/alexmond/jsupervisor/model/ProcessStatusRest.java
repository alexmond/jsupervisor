package org.alexmond.jsupervisor.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.alexmond.jsupervisor.config.ProcessConfig;
import org.alexmond.jsupervisor.repository.RunningProcess;

import java.time.Duration;
import java.time.LocalDateTime;

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

    @Schema(description = "Name of the process", example = "myapp")
    private String name;

    @Schema(description = "Current status of the process (e.g., running, stopped, failed)")
    private ProcessStatus status;

    @Schema(description = "Process ID assigned by the operating system", example = "1234")
    private Long pid;

    @Schema(description = "Timestamp when the process was started")
    private LocalDateTime startTime;

    @Schema(description = "Timestamp when the process ended")
    private LocalDateTime endTime;

    @Schema(description = "Process exit code", example = "0")
    private Integer exitCode;

    @Schema(description = "Process runtime duration", format = "duration")
    private Duration processRuntime;

    @Schema(description = "Formatted process uptime", example = "2d 5h 30m")
    private String processUptime;

    @Schema(description = "Process configuration settings")
    private ProcessConfig processConfig;

    @Schema(description = "File path where the standard output (stdout) of the process will be logged. If not specified, stdout will be inherited from the parent process.")
    private String stdoutLogfile;

    @Schema(description = "File path where the standard error (stderr) of the process will be logged. If not specified, stderr will be inherited from the parent process.")
    private String stderrLogfile;
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
}
