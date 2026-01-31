package org.alexmond.jsupervisor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasItems;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SwaggerConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testOpenApiSpecContainsProcessEnums() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/v1/events/by-process/{processName}'].get.parameters[?(@.name=='processName')].schema.enum[*]").value(hasItems("myapp", "anotherapp")))
                .andExpect(jsonPath("$.paths['/api/v1/processes/start/{name}'].post.parameters[?(@.name=='name')].schema.enum[*]").value(hasItems("myapp", "anotherapp")));
    }
}
