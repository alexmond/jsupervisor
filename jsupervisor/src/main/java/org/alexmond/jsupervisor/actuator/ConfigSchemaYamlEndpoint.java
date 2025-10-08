package org.alexmond.jsupervisor.actuator;

import lombok.RequiredArgsConstructor;
import org.alexmond.config.json.schema.service.JsonSchemaService;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

/**
 * Spring Boot Actuator endpoint that exposes the application's configuration schema
 * in YAML format. This endpoint allows clients to retrieve the complete configuration
 * documentation through the /actuator/config-schema.yaml endpoint.
 *
 * @since 0.0.2
 */
@Component
@Endpoint(id = "config-schema.yaml")
@RequiredArgsConstructor
public class ConfigSchemaYamlEndpoint {

    /**
     * Service component that generates JSON schema representations
     * of application configuration properties.
     */
    private final JsonSchemaService jsonSchemaService;

    /**
     * Retrieves the complete configuration schema in YAML format.
     *
     * @return String containing the YAML representation of the configuration schema
     */
    @ReadOperation
    public String schema() {
        return jsonSchemaService.generateFullSchemaYaml();
    }
}