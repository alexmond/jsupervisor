package org.alexmond.supervisor.healthcheck;

import lombok.extern.slf4j.Slf4j;
import org.alexmond.supervisor.config.ActuatorHealthCheckConfig;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.Duration;
import java.util.Map;

@Slf4j
public class ActuatorHealthCheck implements HealthCheck {
    private final RestClient restClient;
    private boolean cachedHealth = false;
    private int consecutiveSuccesses = 0;
    private int consecutiveFailures = 0;
    ActuatorHealthCheckConfig config;

    public ActuatorHealthCheck(ActuatorHealthCheckConfig config) {
        this.config = config;
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(Duration.ofSeconds(config.getTimeoutSeconds()));
        clientHttpRequestFactory.setConnectionRequestTimeout(Duration.ofSeconds(config.getTimeoutSeconds()));


        this.restClient = RestClient.builder()
                .requestFactory(clientHttpRequestFactory)
                .baseUrl(config.getActuatorHealthUrl())
                .build();
    }

    @Override
    public boolean check() {
        return cachedHealth;
    }

    @Override
    public void run() {
        
        try {
            log.info("Performing health check");
            var response = restClient.get().retrieve().body(Map.class);
            String status = response != null && response.containsKey("status") ? response.get("status").toString() : "UNKNOWN";
            log.info("Health check status: {}", status);
            boolean currentHealth = "UP".equalsIgnoreCase(status);

            if (currentHealth) {
                consecutiveSuccesses++;
                consecutiveFailures = 0;
                if (consecutiveSuccesses >= config.getSuccessThreshold()) {
                    cachedHealth = true;
                }
            } else {
                consecutiveFailures++;
                consecutiveSuccesses = 0;
                if (consecutiveFailures >= config.getFailureThreshold()) {
                    cachedHealth = false;
                }
            }
        } catch (RestClientException ex) {
            log.error("Health check failed", ex);
            consecutiveFailures++;
            consecutiveSuccesses = 0;
            if (consecutiveFailures >= config.getFailureThreshold()) {
                cachedHealth = false;
            }
        }
    }
}
