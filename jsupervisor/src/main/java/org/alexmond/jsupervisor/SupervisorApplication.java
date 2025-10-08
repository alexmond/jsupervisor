/**
 * Main package for JSupervisor application containing core application components.
 */
package org.alexmond.jsupervisor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main Spring Boot application class for JSupervisor - a process supervisor for managing and monitoring application processes.
 * <p>
 * This application provides a comprehensive solution for:
 * <ul>
 *     <li>Process management and monitoring</li>
 *     <li>Web-based user interface for process control</li>
 *     <li>RESTful API for programmatic access</li>
 *     <li>Asynchronous operations support</li>
 *     <li>Scheduled task execution</li>
 * </ul>
 *
 * @see SpringBootApplication
 * @see EnableAsync
 * @see EnableScheduling
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class SupervisorApplication {

    /**
     * The main entry point for the JSupervisor application.
     * Launches the Spring Boot application with the provided command line arguments.
     *
     * @param args command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(SupervisorApplication.class, args);
    }

}
