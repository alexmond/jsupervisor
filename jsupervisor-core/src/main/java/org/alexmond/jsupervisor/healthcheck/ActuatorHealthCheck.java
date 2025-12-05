package org.alexmond.jsupervisor.healthcheck;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.ActuatorHealthCheckConfig;
import org.alexmond.jsupervisor.model.ProcessStatus;
import org.alexmond.jsupervisor.model.RunningProcess;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.Map;

/**
 * Health check implementation that monitors application health through Spring Boot Actuator's health endpoint.
 * This implementation supports consecutive success/failure thresholds and caches the health state.
 */
@Slf4j
public class ActuatorHealthCheck implements HealthCheck {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    ActuatorHealthCheckConfig config;
    private boolean cachedHealth = false;
    private int consecutiveSuccesses = 0;
    private int consecutiveFailures = 0;
    private RunningProcess runningProcess;

    /**
     * Creates a new ActuatorHealthCheck instance.
     *
     * @param config         Configuration containing actuator endpoint URL, timeout, and threshold settings
     * @param runningProcess The process being monitored
     */
    public ActuatorHealthCheck(ActuatorHealthCheckConfig config, RunningProcess runningProcess) {
        this.config = config;
        this.runningProcess = runningProcess;
        this.httpClient = createHttpClient(config);
        this.objectMapper = new ObjectMapper();
    }

    private HttpClient createHttpClient(ActuatorHealthCheckConfig config) {
        HttpClient.Builder builder = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(config.getTimeoutSeconds()));

        if (config.isIgnoreSslErrors()) {
            try {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }}, new SecureRandom());

                builder.sslContext(sslContext)
                        .sslParameters(new SSLParameters());
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                log.warn("Failed to initialize SSL context for ignoring certificate validation: {}", e.getMessage());
            }
        }

        return builder.build();
    }

    /**
     * Returns the cached health state of the monitored application.
     *
     * @return true if the application is considered healthy, false otherwise
     */
    @Override
    public boolean check() {
        return cachedHealth;
    }

    /**
     * Performs the health check by calling the actuator endpoint.
     * Updates the cached health state based on consecutive successes/failures
     * and configured thresholds.
     */
    @Override
    public void run() {
        log.debug("Performing health check");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(config.getActuatorHealthUrl()))
                .GET()
                .build();
        Map response = null;
        try {
            var httpResponse = httpClient.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() != 200) {
                response = Map.of("status", "DOWN");
            } else {
                response = objectMapper.readValue(httpResponse.body(), Map.class);
            }
        } catch (Exception e) {
            log.error("Failed to execute health check request: {}", e.toString());
            response = Map.of("status", "DOWN");
        }
        String status = (response != null && response.containsKey("status")) ? String.valueOf(response.get("status")) : "DOWN";
        log.debug("Health check status: {}", status);
        boolean currentHealth = "UP".equalsIgnoreCase(status);

        if (currentHealth) {
            consecutiveSuccesses++;
            consecutiveFailures = 0;
            if (consecutiveSuccesses >= config.getSuccessThreshold()) {
                cachedHealth = true;
                runningProcess.setProcessStatus(ProcessStatus.healthy);
            }
        } else {
            consecutiveFailures++;
            consecutiveSuccesses = 0;
            if (consecutiveFailures >= config.getFailureThreshold()) {
                cachedHealth = false;
                runningProcess.setProcessStatus(ProcessStatus.unhealthy);
            }
        }
    }
}
