package org.alexmond.supervisor.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration class for process execution settings.
 * Contains settings for command execution, environment variables,
 * working directory and logging configuration.
 */
@Data
@Validated
//@AllArgsConstructor
public class ProcessConfig {
    /**
     * The command to be executed. This field is required and cannot be empty.
     * Can be either a full path to an executable or a command available in the system PATH.
     */
    @NotBlank(message = "Command must not be empty")
    private String command;

    /**
     * The working directory where the process will be executed.
     * If not specified, the current working directory will be used.
     */
    private String workingDirectory;

    /**
     * URL for health checking the process.
     * Should be a valid URL that the process exposes for health monitoring.
     */
    @URL(message = "Health URL must be a valid URL")
    private String healthUrl;

    /**
     * Environment variables to be set for the process.
     * Keys represent environment variable names, values represent their corresponding values.
     */
    private Map<String, String> env = new HashMap<>();

    /**
     * Command-line arguments to be passed to the process.
     * Each element in the list represents a single argument.
     */
    private List<String> args = new ArrayList<>();

    /**
     * File path where the standard output (stdout) of the process will be logged.
     * If not specified, stdout will be inherited from the parent process.
     */
    private String stdoutLogfile;

    /**
     * File path where the standard error (stderr) of the process will be logged.
     * If not specified, stderr will be inherited from the parent process.
     */
    private String stderrLogfile;

    /**
     * Determines if log files should be appended to or overwritten.
     * If true, new log entries will be appended to existing log files.
     * If false, existing log files will be overwritten when the process starts.
     */
    private Boolean appendLog = true;

    /**
     * Determines if the error stream should be redirected to the output stream.
     * If true, stderr will be merged with stdout.
     * If false, stderr will be written to a separate file if specified.
     */
    private Boolean redirectErrorStream = true;

    /**
     * The maximum time to wait for the process to shut down gracefully.
     * After this duration, the process will be forcefully terminated if still running.
     * Defaults to 5 seconds.
     */
    private Duration shutdownTimeout = Duration.ofSeconds(5);

}