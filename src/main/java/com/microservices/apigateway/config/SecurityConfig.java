package com.microservices.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${keycloak.jwks-uri:http://localhost:8080/realms/microservices-realm/protocol/openid-connect/certs}")
    private String jwksUri;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .cors(cors -> cors.configurationSource(exchange -> {
                org.springframework.web.cors.CorsConfiguration config =
                        new org.springframework.web.cors.CorsConfiguration();
                config.setAllowedOrigins(java.util.List.of("*"));
                config.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(java.util.List.of("*"));
                config.setAllowCredentials(false);
                return config;
            }))
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .pathMatchers("/actuator/**", "/health", "/api/auth/register", "/api/auth/login", "/api/auth/refresh").permitAll()
                .pathMatchers("/api/courses/videos/*/stream").permitAll()
                .anyExchange().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwkSetUri(jwksUri))
            );

        return http.build();
    }
}
