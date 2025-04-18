package com.danrley.gestao_tarefas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

  final String securitySchemeName = "bearerAuth";

  @Bean
  OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Gestão de Tarefas API")
            .version("1.0.0")
            .description("API para gerenciamento de tarefas"))
        .components(new Components()
            .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")));
    // .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
    // Define globalmente a necessidade de autenticação
  }
}
