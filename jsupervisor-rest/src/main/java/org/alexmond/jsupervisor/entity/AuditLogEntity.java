package org.alexmond.jsupervisor.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity for audit logging of all supervisor actions.
 * Tracks who did what, when, and why for compliance and troubleshooting.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Audit log entity")
public class AuditLogEntity extends BaseEntity {

    /**
     * Action timestamp
     */
    @Schema(description = "Action timestamp")
    private LocalDateTime timestamp;

    /**
     * Action type
     */
    @Schema(description = "Action type")
    private ActionType actionType;

    /**
     * Entity type affected
     */
    @Schema(description = "Entity type", example = "PROCESS")
    private String entityType;

    /**
     * Entity ID affected
     */
    @Schema(description = "Entity ID")
    private Long entityId;

    /**
     * Entity name
     */
    @Schema(description = "Entity name", example = "webapp")
    private String entityName;

    /**
     * User who performed action
     */
    @Schema(description = "User", example = "admin")
    private String user;

    /**
     * Source IP address
     */
    @Schema(description = "Source IP", example = "192.168.1.100")
    private String sourceIp;

    /**
     * Action description
     */
    @Schema(description = "Action description", example = "Started process webapp")
    private String description;

    /**
     * Previous value/state (JSON)
     */
    @Schema(description = "Previous value (JSON)")
    private String previousValue;

    /**
     * New value/state (JSON)
     */
    @Schema(description = "New value (JSON)")
    private String newValue;

    /**
     * Action result
     */
    @Schema(description = "Action result")
    private ActionResult result;

    /**
     * Error message if action failed
     */
    @Schema(description = "Error message")
    private String errorMessage;

    /**
     * Reference to supervisor entity
     */
    @Schema(description = "Supervisor ID")
    private Long supervisorId;

    public enum ActionType {
        PROCESS_START,
        PROCESS_STOP,
        PROCESS_RESTART,
        CONFIGURATION_UPDATE,
        HEALTH_CHECK,
        SUPERVISOR_START,
        SUPERVISOR_STOP,
        USER_LOGIN,
        USER_LOGOUT,
        API_ACCESS
    }

    public enum ActionResult {
        SUCCESS,
        FAILURE,
        PARTIAL,
        PENDING
    }
}
