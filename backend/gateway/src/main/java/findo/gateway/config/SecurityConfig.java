package findo.gateway.config;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

        @Bean
        public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
                http
                                .csrf(csrfCustomizer -> csrfCustomizer.disable()) // Disable CSRF protection
                                .authorizeExchange(exchange -> exchange
                                                .pathMatchers(
                                                                "/api/v1/auth/register",
                                                                "/api/v1/auth/login",
                                                                "/api/v1/schedules/grouped",
                                                                "/api/v1/schedules/detail/**")
                                                .permitAll() // Allow public access to these endpoints
                                                .pathMatchers("/api/v1/bookings/admin/**")
                                                .hasRole("ADMIN") // Only accessible with ADMIN role
                                                .pathMatchers("/api/v1/bookings/customer/**").hasRole("ROLE_CUSTOMER") // Accessible
                                                                                                                       // to
                                                                                                                       // customers
                                                .anyExchange().authenticated() // Protect all other endpoints
                                )
                                .oauth2ResourceServer(oauth2 -> oauth2
                                                .jwt(jwtConfigurer -> jwtConfigurer
                                                                .jwtAuthenticationConverter(
                                                                                grantedAuthoritiesExtractor()))); // Enable
                                                                                                                  // JWT-based
                                                                                                                  // authentication
                                                                                                                  // for
                                                                                                                  // protected
                                                                                                                  // endpoints

                return http.build();
        }

        private ReactiveJwtAuthenticationConverterAdapter grantedAuthoritiesExtractor() {
                JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
                jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(this::extractAuthorities);
                return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
        }

        private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
                List<String> roles = jwt.getClaimAsStringList("roles");
                return roles.stream()
                                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                                .collect(Collectors.toList());
        }
}
