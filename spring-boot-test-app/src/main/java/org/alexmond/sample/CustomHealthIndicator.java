package org.alexmond.sample;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@Data
@Slf4j
public class CustomHealthIndicator implements HealthIndicator {
    boolean healthy = true;
    @Override
    public Health health() {

        if (healthy) {
            return Health.up().withDetail("custom", "Everything is OK!").build();
        } else {
            return Health.down().withDetail("custom", "Something is wrong!").build();
        }
    }
}
