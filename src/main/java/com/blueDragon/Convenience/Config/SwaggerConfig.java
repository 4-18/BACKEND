package com.blueDragon.Convenience.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "2024 4호선톤 Convenience API", description = "2024 4호선톤 백엔드 Convenience API 명세서", version = "v1"),
        servers = {
                @Server(url = "http://localhost:8080"),
                @Server(url = "http://54.180.171.247:8080")
        }
)
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi SwaggerOpenApi() {
        return GroupedOpenApi.builder()
                .group("Swagger-api")
                .pathsToMatch("/**")
                .build();
    }
}