package org.alexmond.jsupervisor.healthcheck;

import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.ActuatorHealthCheckConfig;
import org.alexmond.jsupervisor.model.ProcessStatus;
import org.alexmond.jsupervisor.repository.RunningProcess;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.Duration;
import java.util.Map;

@Slf4j
public class ActuatorHealthCheck implements HealthCheck {
    private final RestClient restClient;
    ActuatorHealthCheckConfig config;
    private boolean cachedHealth = false;
    private int consecutiveSuccesses = 0;
    private int consecutiveFailures = 0;
    private RunningProcess runningProcess;

    public ActuatorHealthCheck(ActuatorHealthCheckConfig config, RunningProcess runningProcess) {
        this.config = config;
        this.runningProcess = runningProcess;
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
                if (consecutiveSuccesses >= config.getSuccessThreshold() && !cachedHealth) {
                    cachedHealth = true;
                    runningProcess.setProcessStatus(ProcessStatus.healthy);
                }
            } else {
                consecutiveFailures++;
                consecutiveSuccesses = 0;
                if (consecutiveFailures >= config.getFailureThreshold() && cachedHealth) {
                    cachedHealth = false;
                    runningProcess.setProcessStatus(ProcessStatus.unhealthy);
                }
            }
        } catch (RestClientException ex) {
            log.error("Health check failed {}", ex.toString());
            consecutiveFailures++;
            consecutiveSuccesses = 0;
            if (consecutiveFailures >= config.getFailureThreshold() && cachedHealth) {
                cachedHealth = false;
                runningProcess.setProcessStatus(ProcessStatus.unhealthy);
            }
        }
    }
}
