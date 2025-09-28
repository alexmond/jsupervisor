package org.alexmond.supervisor.healthcheck;

import lombok.extern.slf4j.Slf4j;
import org.alexmond.supervisor.config.PortHealthCheckConfig;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

@Slf4j
public class PortHealthCheck implements HealthCheck {
    private boolean cachedHealth = false;
    private PortHealthCheckConfig config;
    private int consecutiveSuccesses = 0;
    private int consecutiveFailures = 0;

    public PortHealthCheck(PortHealthCheckConfig portHealthCheckConfig) {
        config = portHealthCheckConfig;
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
                consecutiveSuccesses++;
                consecutiveFailures = 0;
                if (consecutiveSuccesses >= config.getSuccessThreshold()) {
                    cachedHealth = true;
                }
            }
        } catch (IOException ex) {
            log.error("Health check failed", ex);
            consecutiveFailures++;
            consecutiveSuccesses = 0;
            if (consecutiveFailures >= config.getFailureThreshold()) {
                cachedHealth = false;
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
