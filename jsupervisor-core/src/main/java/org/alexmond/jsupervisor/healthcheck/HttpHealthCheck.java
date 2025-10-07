package org.alexmond.jsupervisor.healthcheck;

import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.HttpHealthCheckConfig;
import org.alexmond.jsupervisor.model.ProcessStatus;
import org.alexmond.jsupervisor.repository.RunningProcess;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.Duration;

@Slf4j
public class HttpHealthCheck implements HealthCheck {
    private final RestClient restClient;
    int successCount = 0;
    int failureCount = 0;
    private boolean cachedHealth = false;
    private HttpHealthCheckConfig config;
    private RunningProcess runningProcess;

    public HttpHealthCheck(HttpHealthCheckConfig config, RunningProcess runningProcess) {
        this.config = config;
        this.runningProcess = runningProcess;

        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(Duration.ofSeconds(config.getTimeoutSeconds()));
        clientHttpRequestFactory.setConnectionRequestTimeout(Duration.ofSeconds(config.getTimeoutSeconds()));


        this.restClient = RestClient.builder()
                .requestFactory(clientHttpRequestFactory)
                .baseUrl(config.getUrl())
                .build();
    }

    @Override
    public boolean check() {
        return cachedHealth;
    }

    @Override
    public void run() {

        try {
            log.debug("Performing health check");
            var status = restClient.get().retrieve().toBodilessEntity().getStatusCode();
            if (status.is2xxSuccessful()) {
                successCount++;
                if (successCount >= config.getSuccessThreshold() && !cachedHealth) {
                    cachedHealth = true;
                    runningProcess.setProcessStatus(ProcessStatus.healthy);
                }
                failureCount = 0;
            } else {
                failureCount++;
                if (failureCount >= config.getFailureThreshold() && cachedHealth) {
                    cachedHealth = false;
                    runningProcess.setProcessStatus(ProcessStatus.unhealthy);
                }
                successCount = 0;
            }
        } catch (RestClientException ex) {
            failureCount++;
            if (failureCount >= config.getFailureThreshold() && cachedHealth) {
                cachedHealth = false;
                runningProcess.setProcessStatus(ProcessStatus.unhealthy);
            }
            successCount = 0;
            log.error("Health check failed", ex);
        }
    }

}
