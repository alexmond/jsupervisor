package org.alexmond.supervisor.healthcheck;

import lombok.extern.slf4j.Slf4j;
import org.alexmond.supervisor.config.HttpHealthCheckConfig;
import org.alexmond.supervisor.model.ProcessStatus;
import org.alexmond.supervisor.repository.RunningProcess;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.Duration;

@Slf4j
public class HttpHealthCheck implements HealthCheck {
    private final RestClient restClient;
    private boolean cachedHealth = false;
    private HttpHealthCheckConfig config;
    private RunningProcess runningProcess;
    int successCount = 0;
    int failureCount = 0;

    public HttpHealthCheck(HttpHealthCheckConfig config, RunningProcess runningProcess) {
        this.config = config;
        this.runningProcess = runningProcess;

        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(Duration.ofSeconds(config.getTimeoutSeconds()));
        clientHttpRequestFactory.setConnectionRequestTimeout(Duration.ofSeconds(config.getTimeoutSeconds()));


        this.restClient = RestClient.builder()
                .requestFactory(clientHttpRequestFactory)
                .baseUrl(config.getURL())
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
