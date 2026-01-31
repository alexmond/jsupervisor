package org.alexmond.jsupervisor.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.alexmond.jsupervisor.config.SupervisorConfig;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenApiCustomizer processNameCustomizer(SupervisorConfig supervisorConfig) {
        return openApi -> {
            Set<String> processNames = supervisorConfig.getProcess().keySet();
            if (processNames.isEmpty()) {
                return;
            }

            openApi.getPaths().forEach((path, pathItem) -> {
                updateOperationProcessNames(pathItem.getGet(), processNames);
                updateOperationProcessNames(pathItem.getPost(), processNames);
                updateOperationProcessNames(pathItem.getPut(), processNames);
                updateOperationProcessNames(pathItem.getDelete(), processNames);
                updateOperationProcessNames(pathItem.getPatch(), processNames);
            });
        };
    }

    private void updateOperationProcessNames(Operation operation, Set<String> processNames) {
        if (operation == null || operation.getParameters() == null) {
            return;
        }

        for (Parameter parameter : operation.getParameters()) {
            if (("processName".equals(parameter.getName()) || "name".equals(parameter.getName()))
                    && ("path".equals(parameter.getIn()) || "query".equals(parameter.getIn()))) {
                Schema schema = parameter.getSchema();
                if (schema != null) {
                    schema.setEnum(new ArrayList<>(processNames));
                }
            }
        }
    }
}
