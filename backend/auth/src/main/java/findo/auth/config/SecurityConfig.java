package findo.auth.config;

import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import findo.auth.utils.PemUtils;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disabling CSRF for stateless API
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers(
                        "/api/v1/auth/register", 
                        "/api/v1/auth/login",
                        "/api/v1/schedules/grouped",
                        "/api/v1/schedules/detail/**").permitAll()
                    .requestMatchers(
                        "/api/v1/movies/**").hasRole("ADMIN") // Allow public access to register and login
                    .anyRequest().authenticated() // Protect all other endpoints
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())); // Enable JWT-based authentication for protected endpoints
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtDecoder jwtDecoder() throws Exception {
        // Loading public key from the PEM file
        ClassPathResource resource = new ClassPathResource("keys/public.pem");
        RSAPublicKey publicKey = PemUtils.readPublicKey(resource.getInputStream());
        return NimbusJwtDecoder.withPublicKey(publicKey).build(); // Building the decoder with the public key
    }

    @Bean
    public JwtEncoder jwtEncoder() throws Exception {
        // Load the private key from the PEM file
        ClassPathResource privateKeyResource = new ClassPathResource("keys/private.pem");
        PrivateKey privateKey = PemUtils.readPrivateKey(privateKeyResource.getInputStream());

        // Load the public key again from the PEM file
        ClassPathResource publicKeyResource = new ClassPathResource("keys/public.pem");
        RSAPublicKey publicKey = PemUtils.readPublicKey(publicKeyResource.getInputStream());

        // Create the RSAKey using both the public and private keys
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .build();

        // Configure the JWT encoder using the RSAKey
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(rsaKey));
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}