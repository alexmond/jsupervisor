package org.alexmond.jsupervisor.actuator;

import lombok.RequiredArgsConstructor;
import org.alexmond.config.json.schema.service.JsonSchemaService;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

/**
 * Spring Boot Actuator endpoint that exposes the application's configuration schema.
 * The endpoint provides access to JSON Schema representation of all available configuration
 * properties that can be used for validation or documentation purposes.
 *
 * @see org.alexmond.config.json.schema.service.JsonSchemaService
 * @since 0.0.2
 */
@Component
@Endpoint(id = "config-schema.json")
@RequiredArgsConstructor
public class ConfigSchemaJsonEndpoint {

    /**
     * Service component responsible for generating JSON Schema representation
     * of the application's configuration metadata.
     */
    private final JsonSchemaService jsonSchemaService;

    /**
     * Generates and returns the JSON schema for all configuration properties.
     *
     * @return A string containing the JSON Schema representation of the configuration
     */
    @ReadOperation
    public String schema() {
        return jsonSchemaService.generateFullSchemaJson();
    }
}