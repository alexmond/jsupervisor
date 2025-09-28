package org.alexmond.supervisor.healthcheck;

public interface HealthCheck extends Runnable {
    public boolean check();

}
