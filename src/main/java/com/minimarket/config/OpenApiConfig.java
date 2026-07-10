package com.minimarket.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion de OpenAPI (springdoc). Define la informacion general de la API
 * y el esquema de seguridad JWT (Bearer) para que Swagger UI muestre el boton
 * "Authorize" y se pueda probar con un token real.
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME = "bearerAuth";

    @Bean
    public OpenAPI miniMarketOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MiniMarket Plus API")
                        .version("1.0.0")
                        .description("API REST del backend de MiniMarket Plus: productos, carrito, "
                                + "inventario, ventas y usuarios. Protegida con JWT y documentada con "
                                + "OpenAPI + HATEOAS.")
                        .contact(new Contact().name("Equipo MiniMarket Plus - PBY2202"))
                        .license(new License().name("Uso academico")))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME, new SecurityScheme()
                                .name(SECURITY_SCHEME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Ingresa el token JWT obtenido en POST /api/auth/login")));
    }
}
