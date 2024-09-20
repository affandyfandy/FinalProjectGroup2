package findo.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.reactive.CorsWebFilter;

@Configuration
public class GatewayConfig {

    @Autowired
    private CorsConfig corsConfig;

    @Bean
    public CorsWebFilter corsWebFilter() {
        return new CorsWebFilter(corsConfig.corsConfigurationSource());
    }

}