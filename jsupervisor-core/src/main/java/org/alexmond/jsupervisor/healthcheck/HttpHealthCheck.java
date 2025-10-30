package org.alexmond.jsupervisor.healthcheck;

import lombok.extern.slf4j.Slf4j;
import org.alexmond.jsupervisor.config.HttpHealthCheckConfig;
import org.alexmond.jsupervisor.model.ProcessStatus;
import org.alexmond.jsupervisor.repository.RunningProcess;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.http.HttpClient;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Duration;

/**
 * Implementation of health check that monitors HTTP endpoints.
 * Performs periodic HTTP requests to configured URL and tracks successful/failed responses
 * to determine if the monitored process is healthy.
 */
@Slf4j
public class HttpHealthCheck implements HealthCheck {
    private final HttpClient httpClient;
    int successCount = 0;
    int failureCount = 0;
    private boolean cachedHealth = false;
    private final HttpHealthCheckConfig config;
    private final RunningProcess runningProcess;

    /**
     * Creates new HTTP health check instance.
     *
     * @param config         Configuration containing URL, timeout and threshold settings
     * @param runningProcess Process being monitored
     */
    public HttpHealthCheck(HttpHealthCheckConfig config, RunningProcess runningProcess) {
        this.config = config;
        this.runningProcess = runningProcess;
        this.httpClient = createHttpClient(config);
        
    }

    private HttpClient createHttpClient(HttpHealthCheckConfig config) {
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
     * Returns the current cached health status.
     *
     * @return true if endpoint is considered healthy, false otherwise
     */
    @Override
    public boolean check() {
        return cachedHealth;
    }

    /**
     * Performs the HTTP health check request.
     * Updates success/failure counts and cached health status based on the response.
     * Updates process status when health state changes.
     */
    @Override
    public void run() {
        try {
            log.debug("Performing health check");
            var request = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create(config.getUrl()))
                    .timeout(Duration.ofSeconds(config.getTimeoutSeconds()))
                    .GET()
                    .build();

            var response = httpClient.send(request, java.net.http.HttpResponse.BodyHandlers.discarding());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
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
        } catch (Exception ex) {
            failureCount++;
            if (failureCount >= config.getFailureThreshold() && cachedHealth) {
                cachedHealth = false;
                runningProcess.setProcessStatus(ProcessStatus.unhealthy);
            }
            successCount = 0;
            log.warn("Health check failed {}", ex.getMessage());
        }
    }
}
