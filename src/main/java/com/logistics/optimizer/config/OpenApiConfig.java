package com.logistics.optimizer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI optimizerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Logistics Route Optimizer API")
                        .version("1.0.0")
                        .description("Enterprise-grade logistics optimization microservice for Dubai recruiters demo.")
                        .contact(new Contact().name("AI Logistics Team").email("ai-logistics@example.com"))
                        .license(new License().name("Apache 2.0")));
    }
}

