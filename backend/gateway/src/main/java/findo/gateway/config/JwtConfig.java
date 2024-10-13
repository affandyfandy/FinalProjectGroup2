package findo.gateway.config;

import java.security.interfaces.RSAPublicKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import findo.gateway.utils.PemUtils;

@Configuration
public class JwtConfig {

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() throws Exception {
        ClassPathResource resource = new ClassPathResource("keys/public.pem");
        RSAPublicKey publicKey = PemUtils.readPublicKey(resource.getInputStream());
        return NimbusReactiveJwtDecoder.withPublicKey(publicKey).build();
    }
}