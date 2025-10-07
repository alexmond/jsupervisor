package org.alexmond.jsupervisor.actuator;

import lombok.RequiredArgsConstructor;
import org.alexmond.config.json.schema.service.JsonSchemaService;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "config-schema.json")
@RequiredArgsConstructor
public class ConfigSchemaJsonEndpoint {

    private final JsonSchemaService jsonSchemaService;

    @ReadOperation
    public String schema() {
        return jsonSchemaService.generateFullSchemaJson();
    }
}