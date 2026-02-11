package org.example.ninh.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI openAPI(@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri) {
        String normalizedIssuer = issuerUri.endsWith("/")
                ? issuerUri.substring(0, issuerUri.length() - 1)
                : issuerUri;

        String tokenUrl = normalizedIssuer + "/protocol/openid-connect/token";

        SecurityScheme oauth2ClientCredentials = new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .description("OAuth2 client credentials flow")
                .flows(new OAuthFlows().clientCredentials(
                        new OAuthFlow()
                                .tokenUrl(tokenUrl)
                ));

        SecurityScheme bearerScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Paste a JWT bearer token manually");

        return new OpenAPI()
                .info(new Info()
                        .title("Simple API")
                        .version("v1"))
                .components(new Components()
                        .addSecuritySchemes("oauth2ClientCredentials", oauth2ClientCredentials)
                        .addSecuritySchemes("bearerAuth", bearerScheme))
                .addSecurityItem(new SecurityRequirement().addList("oauth2ClientCredentials"));
    }
}
