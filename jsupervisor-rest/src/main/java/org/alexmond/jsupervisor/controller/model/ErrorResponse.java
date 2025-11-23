package org.alexmond.jsupervisor.controller.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

@Value
@Builder
@Schema(name = "ErrorResponse", description = "Standard error response for REST APIs.")
public class ErrorResponse {

    @Schema(description = "Timestamp when the error occurred", example = "2025-11-17T10:15:30Z")
    OffsetDateTime timestamp;

    @Schema(description = "Request path where the error occurred", example = "/api/v1/processes")
    String path;

    @Schema(description = "Short error identifier", example = "BAD_REQUEST")
    String error;

    @Schema(description = "Human readable error message", example = "Invalid process ID")
    String message;

    @Schema(description = "Optional application-specific error code", example = "JSUPERVISOR-001")
    String code;
}
