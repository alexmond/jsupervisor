package org.alexmond;

import org.alexmond.model.HealthStatusResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestHealthEndpoint {

    @GetMapping("/healthUp")
    public ResponseEntity<HealthStatusResponse> getHealthStatusUp() {
        HealthStatusResponse response = new HealthStatusResponse();
        response.setStatus("UP");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/healthDown")
    public ResponseEntity<HealthStatusResponse> getHealthStatusDown() {
        HealthStatusResponse response = new HealthStatusResponse();
        response.setStatus("DOWN");
        return ResponseEntity.ok(response);
    }
}
