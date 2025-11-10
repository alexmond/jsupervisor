package org.alexmond.jsupervisor.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity representing a JSupervisor instance.
 * Contains information about the supervisor node and its configuration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Supervisor node entity")
public class SupervisorEntity extends BaseEntity {

    /**
     * Unique name of the supervisor node
     */
    @Schema(description = "Unique supervisor node name", example = "production-supervisor-01")
    private String nodeName;

    /**
     * Description of the supervisor node
     */
    @Schema(description = "Supervisor node description", example = "Production environment supervisor")
    private String description;

    /**
     * Whether the supervisor is currently enabled
     */
    @Schema(description = "Whether supervisor is enabled", example = "true")
    private Boolean enabled;

    /**
     * Hostname where the supervisor is running
     */
    @Schema(description = "Host name", example = "server-01.example.com")
    private String hostname;

    /**
     * IP address of the supervisor host
     */
    @Schema(description = "IP address", example = "192.168.1.100")
    private String ipAddress;

    /**
     * Port number for the supervisor API
     */
    @Schema(description = "API port number", example = "8086")
    private Integer port;

    /**
     * Supervisor version
     */
//    @Schema(description = "Supervisor version", example = "1.0.0")
//    private String version;

    /**
     * Total number of configured processes
     */
    @Schema(description = "Total configured processes", example = "5")
    private Integer totalProcesses;

    /**
     * Number of currently running processes
     */
    @Schema(description = "Currently running processes", example = "3")
    private Integer runningProcesses;

    /**
     * Supervisor startup time
     */
    @Schema(description = "Supervisor startup time")
    private LocalDateTime startupTime;

    /**
     * Last health check time
     */
    @Schema(description = "Last health check time")
    private LocalDateTime lastHealthCheck;

    /**
     * Health status of the supervisor
     */
    @Schema(description = "Health status", example = "HEALTHY")
    private HealthStatus healthStatus;

    public enum HealthStatus {
        HEALTHY,
        DEGRADED,
        UNHEALTHY,
        UNKNOWN
    }
}
