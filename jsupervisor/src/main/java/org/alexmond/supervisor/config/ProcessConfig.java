package org.alexmond.supervisor.config;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
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
@Schema(description = "Configuration class for process execution settings. Contains settings for command execution, environment variables, working directory and logging configuration.")
//@AllArgsConstructor
public class ProcessConfig {
    /**
     * The command to be executed. This field is required and cannot be empty.
     * Can be either a full path to an executable or a command available in the system PATH.
     */
    @Schema(description = "The command to be executed. Can be either a full path to an executable or a command available in the system PATH.")
    @NotBlank(message = "Command must not be empty")
    private String command;

    /**
     * The working directory where the process will be executed.
     * If not specified, the current working directory will be used.
     */
    @Schema(description = "The working directory where the process will be executed. If not specified, the current working directory will be used.")
    private String workingDirectory;

    /**
     * Environment variables to be set for the process.
     * Keys represent environment variable names, values represent their corresponding values.
     */
    @Schema(description = "Environment variables to be set for the process. Keys represent environment variable names, values represent their corresponding values.")
    private Map<String, String> env = new HashMap<>();

    /**
     * Command-line arguments to be passed to the process.
     * Each element in the list represents a single argument.
     */
    @Schema(description = "Command-line arguments to be passed to the process. Each element in the list represents a single argument.")
    private List<String> args = new ArrayList<>();

    /**
     * File path where the standard output (stdout) of the process will be logged.
     * If not specified, stdout will be inherited from the parent process.
     */
    @Schema(description = "File path where the standard output (stdout) of the process will be logged. If not specified, stdout will be inherited from the parent process.")
    private String stdoutLogfile;

    /**
     * File path where the standard error (stderr) of the process will be logged.
     * If not specified, stderr will be inherited from the parent process.
     */
    @Schema(description = "File path where the standard error (stderr) of the process will be logged. If not specified, stderr will be inherited from the parent process.")
    private String stderrLogfile;

    /**
     * Determines if log files should be appended to or overwritten.
     * If true, new log entries will be appended to existing log files.
     * If false, existing log files will be overwritten when the process starts.
     */
    @Schema(description = "Determines if log files should be appended to or overwritten. If true, new log entries will be appended to existing log files. If false, existing log files will be overwritten when the process starts.", defaultValue = "true")
    private Boolean appendLog = true;

    /**
     * Determines if the error stream should be redirected to the output stream.
     * If true, stderr will be merged with stdout.
     * If false, stderr will be written to a separate file if specified.
     */
    @Schema(description = "Determines if the error stream should be redirected to the output stream. If true, stderr will be merged with stdout. If false, stderr will be written to a separate file if specified.", defaultValue = "true")
    private Boolean redirectErrorStream = true;
    
    /**
     * The maximum time to wait for the process to shut down gracefully.
     * After this duration, the process will be forcefully terminated if still running.
     * Defaults to 5 seconds.
     */
    @Schema(description = "The maximum time to wait for the process to shut down gracefully. After this duration, the process will be forcefully terminated if still running. Defaults to 5 seconds.", defaultValue = "5s")
    private Duration shutdownTimeout = Duration.ofSeconds(5);

    /**
     * URL for gracefully shutting down the process.
     * If specified, a request will be sent to this URL before forcefully terminating the process.
     * Example: "http://localhost:8080/actuator/shutdown"
     */
    @Schema(description = "URL for gracefully shutting down the process. If specified, a request will be sent to this URL before forcefully terminating the process.", example = "http://localhost:8080/actuator/shutdown")
    private String shutdownUrl;

    /**
     * URL to of the application 
     */
    @Schema(description = "URL or command to execute the process", example = "http://localhost:8080/app")
    private String url;

    /**
     * Description of the process.
     * Provides human-readable information about the process's purpose.
     */
    @Schema(description = "Description of the process", example = "Main application service")
    private String description;

    /**
     * Type of health check to be performed on the process.
     * Determines which method will be used to monitor process health.
     */
    @Schema(description = "Type of health check to be performed on the process")
    private HealthCheckType healthCheckType= HealthCheckType.NONE;

    /**
     * Configuration for Spring Boot Actuator-based health checks.
     * Used when healthCheckType is set to ACTUATOR.
     */
    @NestedConfigurationProperty
    private ActuatorHealthCheckConfig actuatorHealthCheck;

    /**
     * Configuration for URL-based health checks.
     * Used when healthCheckType is set to HTTP.
     */
    @NestedConfigurationProperty
    private HttpHealthCheckConfig httpHealthCheckConfig;

    /**
     * Configuration for TCP port-based health checks.
     * Used when healthCheckType is set to TCP.
     */
    @NestedConfigurationProperty
    private PortHealthCheckConfig portHealthCheck;

}