package com.microservices.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Value("${AUTH_SERVICE_URL:http://localhost:8084}")
    private String authServiceUrl;

    @Value("${FILE_SERVICE_URL:http://localhost:8081}")
    private String fileServiceUrl;

    @Value("${NOTIFICATION_SERVICE_URL:http://localhost:8082}")
    private String notificationServiceUrl;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()

                .route("auth-service", r -> r
                        .path("/api/auth/**")
                        .uri(authServiceUrl))

                .route("file-service", r -> r
                        .path("/api/files/**")
                        .uri(fileServiceUrl))

                .route("courses-service", r -> r
                        .path("/api/courses/**")
                        .uri(fileServiceUrl))

                .route("notification-service", r -> r
                        .path("/api/notifications/**")
                        .uri(notificationServiceUrl))

                .build();
    }
}
