package org.alexmond.supervisor.healthcheck;

import lombok.extern.slf4j.Slf4j;
import org.alexmond.supervisor.config.PortHealthCheckConfig;
import org.alexmond.supervisor.model.ProcessStatus;
import org.alexmond.supervisor.repository.RunningProcess;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

@Slf4j
public class PortHealthCheck implements HealthCheck {
    private boolean cachedHealth = false;
    private PortHealthCheckConfig config;
    private RunningProcess runningProcess;
    private int successesCount = 0;
    private int failureCount = 0;

    public PortHealthCheck(PortHealthCheckConfig portHealthCheckConfig, RunningProcess runningProcess) {
        config = portHealthCheckConfig;
        this.runningProcess = runningProcess;
    }

    @Override
    public boolean check() {
        return cachedHealth;
    }

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

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(100);
        clientHttpRequestFactory.setConnectionRequestTimeout(70);
        return clientHttpRequestFactory;
    }
}
