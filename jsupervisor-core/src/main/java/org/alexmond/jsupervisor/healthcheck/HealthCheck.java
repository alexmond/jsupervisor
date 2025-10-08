package org.alexmond.jsupervisor.healthcheck;

/**
 * Interface for implementing health check functionality.
 * Classes implementing this interface should provide mechanisms
 * to verify the health status of specific components or services.
 * As it extends Runnable, implementations can be executed in separate threads.
 */
public interface HealthCheck extends Runnable {
    /**
     * Performs a health check and returns the status.
     *
     * @return true if the health check passes, false otherwise
     */
    public boolean check();

}
