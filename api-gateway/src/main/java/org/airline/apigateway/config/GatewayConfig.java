package org.airline.apigateway.config;

import org.airline.apigateway.filter.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private AuthFilter authFilter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r
                        .path("/api/auth/**")
                        .uri("lb://ms-auth"))
                .route("user-service", r -> r
                        .path("/api/users/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://ms-user"))
                .route("flight-service", r -> r
                        .path("/api/flights/**")
                        .uri("lb://ms-flight"))
                .route("search-service", r -> r
                        .path("/api/search/**")
                        .uri("lb://ms-search"))
                .route("booking-service", r -> r
                        .path("/api/bookings/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://ms-booking"))
                .route("payment-service", r -> r
                        .path("/api/payments/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://ms-payment"))
                .route("pricing-service", r -> r
                        .path("/api/pricing/**")
                        .uri("lb://ms-pricing"))
                .build();
    }
}

