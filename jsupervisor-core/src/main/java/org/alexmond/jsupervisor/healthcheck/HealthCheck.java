package org.alexmond.jsupervisor.healthcheck;

public interface HealthCheck extends Runnable {
    public boolean check();

}
