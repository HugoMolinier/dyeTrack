package com.example.dyeTrack.in.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

        @Bean
        public OpenAPI customOpenApi() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("DyeTrack API")
                                                .version("1.0"))
                                // Lock sur tous les endpoints
                                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                                .addSecurityItem(new SecurityRequirement().addList("ApiKeyAuth"))
                                .components(new Components()
                                                .addSecuritySchemes("bearerAuth",
                                                                new SecurityScheme()
                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                .scheme("bearer")
                                                                                .bearerFormat("JWT"))
                                                .addSecuritySchemes("ApiKeyAuth",
                                                                new SecurityScheme()
                                                                                .type(SecurityScheme.Type.APIKEY)
                                                                                .in(SecurityScheme.In.HEADER)
                                                                                .name("X-API-Key")));
        }
}
