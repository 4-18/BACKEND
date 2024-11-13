package com.blueDragon.Convenience.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "2024 4호선톤 Convenience API", description = "2024 4호선톤 백엔드 Convenience API 명세서", version = "v1")
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

    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT";

        // SecurityRequirement 설정
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);

        // Components 설정
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        );

        return new OpenAPI()
                .components(components) // JWT 인증 구성 요소 추가
                .addSecurityItem(securityRequirement) // JWT 보안 요구 사항 추가
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("해커톤 API 명세서")
                        .description("API 명세서")
                        .version("1.0.0"));
    }
}