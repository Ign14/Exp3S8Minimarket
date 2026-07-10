package com.minimarket.docs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Verifica que la documentacion OpenAPI este disponible y que las respuestas
 * incluyan enlaces HATEOAS (_links). Cubre los criterios de OpenAPI y HATEOAS.
 */
@SpringBootTest
@AutoConfigureMockMvc
class DocumentacionApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("La especificacion OpenAPI (/v3/api-docs) esta disponible y tiene la info de la API")
    void openApiDocsDisponible() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi").exists())
                .andExpect(jsonPath("$.info.title").value("MiniMarket Plus API"))
                .andExpect(jsonPath("$.paths['/api/productos']").exists());
    }

    @Test
    @DisplayName("GET /api/productos incluye enlaces HATEOAS (_links.self)")
    void productosIncluyeEnlacesHateoas() throws Exception {
        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @DisplayName("La especificacion documenta el esquema de seguridad JWT (bearerAuth)")
    void openApiDocumentaSeguridadJwt() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.components.securitySchemes.bearerAuth.scheme").value("bearer"));
    }
}
