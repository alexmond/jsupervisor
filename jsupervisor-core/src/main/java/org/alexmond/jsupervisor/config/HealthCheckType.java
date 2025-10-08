package org.alexmond.jsupervisor.config;

/**
 * Represents different types of health checks that can be performed on a monitored process.
 * Each type defines a specific method to verify if a process is functioning correctly.
 */
public enum HealthCheckType {
    /**
     * Health check using Spring Boot Actuator endpoints
     */
    ACTUATOR,
    /**
     * Health check using HTTP requests
     */
    HTTP,
    /**
     * Health check by verifying if a specific port is open
     */
    PORT,
    /**
     * Health check using command line execution
     */
    CMD,
    /**
     * No health check will be performed
     */
    NONE
}