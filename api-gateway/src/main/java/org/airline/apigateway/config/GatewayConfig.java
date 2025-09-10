package org.airline.apigateway.config;

import org.airline.apigateway.filter.AuthFilter;
import org.airline.apigateway.filter.AdminAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class GatewayConfig {

    @Autowired
    private AuthFilter authFilter;

    @Autowired
    private AdminAuthFilter adminAuthFilter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service-public", r -> r
                        .path("/api/auth/register", "/api/auth/login")
                        .uri("lb://ms-auth"))

                .route("auth-service-protected", r -> r
                        .path("/api/auth/logout", "/api/auth/change-password", "/api/auth/validate")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://ms-auth"))

                .route("auth-service-admin", r -> r
                        .path("/api/auth/admin/**")
                        .filters(f -> f.filter(adminAuthFilter))
                        .uri("lb://ms-auth"))

                .route("user-service", r -> r
                        .path("/api/users/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://ms-user"))

                .route("user-admin-service", r -> r
                        .path("/api/admin/users/**")
                        .filters(f -> f.filter(adminAuthFilter))
                        .uri("lb://ms-user"))

                .route("flight-admin-service", r -> r
                        .path("/api/admin/flights/**", "/api/admin/aircrafts/**", "/api/admin/airports/**")
                        .and().not(p -> p.path("/api/admin/flights/internal/**"))
                        .filters(f -> f.filter(adminAuthFilter))
                        .uri("lb://ms-flight"))

                .route("flight-internal-service", r -> r
                        .path("/api/flights/internal/**")
                        .uri("lb://ms-flight"))

                .route("search-service", r -> r
                        .path("/api/search/**")
                        .uri("lb://ms-search"))

                .route("booking-service", r -> r
                        .path("/api/bookings/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://ms-booking"))

                .route("booking-admin-service", r -> r
                        .path("/api/admin/bookings/**")
                        .filters(f -> f.filter(adminAuthFilter))
                        .uri("lb://ms-booking"))

                .route("payment-service", r -> r
                        .path("/api/payments/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://ms-payment"))

                .route("pricing-service", r -> r
                        .path("/api/pricing/**")
                        .uri("lb://ms-pricing"))

                .route("notification-service", r -> r
                        .path("/api/notifications/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://ms-notification"))

                .route("admin-service", r -> r
                        .path("/api/admin/**")
                        .filters(f -> f.filter(adminAuthFilter))
                        .uri("lb://ms-admin"))

                .build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowCredentials(true);
        corsConfig.addAllowedOriginPattern("*");
        corsConfig.addAllowedHeader("*");
        corsConfig.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}