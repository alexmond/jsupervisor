package org.alexmond.sample;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private static final String HELLO_WORLD_MESSAGE = "Hello World";
    private static final String HEALTHY_MESSAGE = "Service marked as healthy.";
    private static final String UNHEALTHY_MESSAGE = "Service marked as unhealthy.";

    private final CustomHealthIndicator customHealthIndicator;

    public TestController(CustomHealthIndicator customHealthIndicator) {
        this.customHealthIndicator = customHealthIndicator;
    }

    @GetMapping("/")
    public String helloWorld() {
        return HELLO_WORLD_MESSAGE;
    }

    @GetMapping("/setUp")
    public ResponseEntity<String> setHealthy() {
        customHealthIndicator.setHealthy(true);
        return ResponseEntity.ok(HEALTHY_MESSAGE);
    }

    @GetMapping("/setDown")
    public ResponseEntity<String> setUnhealthy() {
        customHealthIndicator.setHealthy(false);
        return ResponseEntity.ok(UNHEALTHY_MESSAGE);
    }
}