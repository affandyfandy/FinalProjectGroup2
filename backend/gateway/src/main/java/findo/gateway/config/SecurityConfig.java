package findo.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .csrf(csrfCustomizer -> csrfCustomizer.disable()) // Disable CSRF protection
            .authorizeExchange(exchange ->
                exchange
                    .pathMatchers(
                        "/api/v1/auth/register", 
                        "/api/v1/auth/login",
                        "/api/v1/schedules/grouped",
                        "/api/v1/schedules/detail/**").permitAll() // Allow public access to these endpoints
                    .pathMatchers("/api/v1/movies/**").hasRole("ADMIN") // Only accessible with ADMIN role
                    .anyExchange().authenticated() // Protect all other endpoints
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())); // Enable JWT-based authentication for protected endpoints
        
        return http.build();
    }
}
