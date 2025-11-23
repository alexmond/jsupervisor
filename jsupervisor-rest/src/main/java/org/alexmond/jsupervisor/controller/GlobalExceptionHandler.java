package org.alexmond.jsupervisor.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.alexmond.jsupervisor.controller.model.ErrorResponse;
import org.alexmond.jsupervisor.exception.JSupervisorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JSupervisorException.class)
    @ApiResponse(
            description = "Business error",
            responseCode = "400",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<ErrorResponse> handleJSupervisorException(
            JSupervisorException ex,
            HttpServletRequest request
    ) {
        ErrorResponse body = ErrorResponse.builder()
                .timestamp(OffsetDateTime.now(ZoneOffset.UTC))
                .path(request.getRequestURI())
                .error(ex.getError() != null ? ex.getError() : "J_SUPERVISOR_ERROR")
                .message(ex.getMessage())
                .code(null)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ApiResponse(
            description = "Validation error",
            responseCode = "400",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        String validationMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining("; "));

        ErrorResponse body = ErrorResponse.builder()
                .timestamp(OffsetDateTime.now(ZoneOffset.UTC))
                .path(request.getRequestURI())
                .error("VALIDATION_ERROR")
                .message(validationMessage)
                .code(null)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ApiResponse(
            description = "Malformed request body",
            responseCode = "400",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        ErrorResponse body = ErrorResponse.builder()
                .timestamp(OffsetDateTime.now(ZoneOffset.UTC))
                .path(request.getRequestURI())
                .error("MALFORMED_REQUEST")
                .message("Malformed JSON request body")
                .code(null)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    @ApiResponse(
            description = "Internal server error",
            responseCode = "500",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request
    ) {
        ErrorResponse body = ErrorResponse.builder()
                .timestamp(OffsetDateTime.now(ZoneOffset.UTC))
                .path(request.getRequestURI())
                .error("INTERNAL_SERVER_ERROR")
                .message("Unexpected error occurred")
                .code(null)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private String formatFieldError(FieldError fieldError) {
        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
    }
}
