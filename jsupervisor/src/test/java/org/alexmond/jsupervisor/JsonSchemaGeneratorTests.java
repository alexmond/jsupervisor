package org.alexmond.jsupervisor;

import lombok.extern.slf4j.Slf4j;
import org.alexmond.config.json.schema.config.JsonConfigSchemaConfig;
import org.alexmond.config.json.schema.service.JsonSchemaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@SpringBootTest
class JsonSchemaGeneratorTests {

    @Autowired
    JsonConfigSchemaConfig config;
    @Autowired
    private JsonSchemaService jsonSchemaService;

    @Test
    void generateJsonSchema() throws IOException {

        config.setSchemaId("http://www.alexmond.org/jsupervisor/current/jsupervisor-schema.json");
        var jsonConfigSchemaJson = jsonSchemaService.generateFullSchemaJson();
        config.setSchemaId("http://www.alexmond.org/jsupervisor/current/jsupervisor-schema.yaml");
        var jsonConfigSchemaYaml = jsonSchemaService.generateFullSchemaYaml();
        log.info("Writing json schema");
        Files.writeString(Paths.get("../docs//modules/ROOT/attachments/jsupervisor-schema.json"), jsonConfigSchemaJson, StandardCharsets.UTF_8);

        log.info("Writing yaml schema");
        Files.writeString(Paths.get("../docs//modules/ROOT/attachments/jsupervisor-schema.yaml"), jsonConfigSchemaYaml, StandardCharsets.UTF_8);
    }
}