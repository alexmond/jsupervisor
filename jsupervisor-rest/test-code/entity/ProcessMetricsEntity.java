package org.alexmond.jsupervisor.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity representing process resource usage metrics at a specific point in time.
 * Used for historical tracking and monitoring.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Process metrics entity")
public class ProcessMetricsEntity extends BaseEntity {

    /**
     * Reference to process entity
     */
    @Schema(description = "Process ID")
    private Long processId;

    /**
     * Process name
     */
    @Schema(description = "Process name", example = "webapp")
    private String processName;

    /**
     * Metrics timestamp
     */
    @Schema(description = "Metrics timestamp")
    private LocalDateTime timestamp;

    /**
     * CPU usage percentage
     */
    @Schema(description = "CPU usage percentage", example = "25.5")
    private Double cpuUsagePercent;

    /**
     * CPU time in milliseconds
     */
    @Schema(description = "CPU time in ms", example = "15000")
    private Long cpuTimeMs;

    /**
     * Memory usage in bytes
     */
    @Schema(description = "Memory usage in bytes", example = "536870912")
    private Long memoryUsageBytes;

    /**
     * Resident Set Size (RSS) in bytes
     */
    @Schema(description = "RSS in bytes", example = "536870912")
    private Long rssBytes;

    /**
     * Virtual memory size in bytes
     */
    @Schema(description = "Virtual memory in bytes", example = "1073741824")
    private Long virtualMemoryBytes;

    /**
     * Number of open file descriptors
     */
    @Schema(description = "Open file descriptors", example = "50")
    private Integer openFileDescriptors;

    /**
     * Number of threads
     */
    @Schema(description = "Thread count", example = "10")
    private Integer threadCount;

    /**
     * Disk read bytes
     */
    @Schema(description = "Disk read bytes", example = "1048576")
    private Long diskReadBytes;

    /**
     * Disk write bytes
     */
    @Schema(description = "Disk write bytes", example = "2097152")
    private Long diskWriteBytes;

    /**
     * Network bytes received
     */
    @Schema(description = "Network bytes received", example = "5242880")
    private Long networkBytesReceived;

    /**
     * Network bytes sent
     */
    @Schema(description = "Network bytes sent", example = "3145728")
    private Long networkBytesSent;

    /**
     * Process uptime in seconds
     */
    @Schema(description = "Uptime in seconds", example = "3600")
    private Long uptimeSeconds;

    /**
     * Reference to supervisor entity
     */
    @Schema(description = "Supervisor ID")
    private Long supervisorId;
}
