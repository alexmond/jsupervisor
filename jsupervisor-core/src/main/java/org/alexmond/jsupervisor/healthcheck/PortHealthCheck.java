package org.alexmond.jsupervisor.healthcheck;

import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.PortHealthCheckConfig;
import org.alexmond.jsupervisor.model.ProcessStatus;
import org.alexmond.jsupervisor.repository.RunningProcess;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Health check implementation that monitors a specific TCP port on a host.
 * Tracks successful and failed connection attempts to determine the health status
 * of the monitored process based on configured thresholds.
 */
@Slf4j
public class PortHealthCheck implements HealthCheck {
    private boolean cachedHealth = false;
    private PortHealthCheckConfig config;
    private RunningProcess runningProcess;
    private int successesCount = 0;
    private int failureCount = 0;

    /**
     * Creates a new PortHealthCheck instance.
     *
     * @param portHealthCheckConfig Configuration for the port health check including host, port, and thresholds
     * @param runningProcess        The process being monitored
     */
    public PortHealthCheck(PortHealthCheckConfig portHealthCheckConfig, RunningProcess runningProcess) {
        config = portHealthCheckConfig;
        this.runningProcess = runningProcess;
    }

    /**
     * Returns the current cached health status.
     *
     * @return true if the monitored port is considered healthy, false otherwise
     */
    @Override
    public boolean check() {
        return cachedHealth;
    }

    /**
     * Performs the health check by attempting to connect to the configured port.
     * Updates the health status based on successful/failed connection attempts
     * and configured thresholds.
     */
    @Override
    public void run() {
        try {
            log.info("Performing health check");
            try (var socket = new Socket()) {
                socket.connect(new InetSocketAddress(config.getHost(), config.getPort()), config.getTimeoutSeconds());
                successesCount++;
                failureCount = 0;
                if (successesCount >= config.getSuccessThreshold() && !cachedHealth) {
                    cachedHealth = true;
                    runningProcess.setProcessStatus(ProcessStatus.healthy);
                }
            }
        } catch (IOException ex) {
            log.error("Health check failed", ex);
            failureCount++;
            successesCount = 0;
            if (failureCount >= config.getFailureThreshold() && cachedHealth) {
                cachedHealth = false;
                runningProcess.setProcessStatus(ProcessStatus.unhealthy);
            }
        }
    }

}
