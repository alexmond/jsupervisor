package org.alexmond.jsupervisor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for the RestProcessController class.
 * This test ensures the correct functionality of the startAllProcess method.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ExportOpenApiSpecTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void generateOpenApiSpecJson() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andDo(print())
                .andDo(result -> {
                    String content = result.getResponse().getContentAsString();
                    ObjectMapper mapper = new ObjectMapper();
                    Object json = mapper.readValue(content, Object.class);
                    String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
                    Files.write(Paths.get("../docs/modules/ROOT/attachments/openapi.json"), prettyJson.getBytes());
                })
                .andExpect(status().isOk());
    }

    @Test
    void generateOpenApiSpecYaml() throws Exception {
        mockMvc.perform(get("/v3/api-docs.yaml"))
                .andExpect(status().isOk())
                .andDo(result2 -> {
                    String yamlContent = result2.getResponse().getContentAsString();
                    Files.write(Paths.get("../docs/modules/ROOT/attachments/openapi.yaml"), yamlContent.getBytes());
                });
    }


}