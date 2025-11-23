package org.alexmond.jsupervisor.service;

/**
 * Interface for managing the lifecycle of processes in the supervisor system.
 * Responsible for starting, stopping, and restarting processes according to their configuration.
 */
public interface ProcessManagerInteface {

    /**
     * Restarts a process by stopping it and then starting it again.
     * This operation is performed asynchronously.
     *
     * @param name The name of the process to restart
     */
    void restartProcess(String name);

    /**
     * Starts a process with the given name according to its configuration.
     * Handles process creation, output redirection, environment setup, and monitoring.
     * This operation is performed asynchronously.
     *
     * @param name The name of the process to start
     * @throws org.alexmond.jsupervisor.exception.JSupervisorException if no process configuration is found for the given name
     */
    void startProcess(String name);

    /**
     * Stops a running process with the given name.
     * Attempts graceful shutdown first, followed by force kill if necessary.
     * This operation is performed asynchronously.
     *
     * @param name The name of the process to stop
     */
    void stopProcess(String name);
}
