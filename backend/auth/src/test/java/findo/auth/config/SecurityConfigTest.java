package findo.auth.config;

import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import findo.auth.utils.PemUtils;

@SpringBootTest(classes = SecurityConfig.class)
public class SecurityConfigTest {

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @BeforeEach
    public void setup() throws Exception {
        PrivateKey privateKey = PemUtils.readPrivateKey(new ClassPathResource("keys/private.pem").getInputStream());
        RSAPublicKey publicKey = PemUtils.readPublicKey(new ClassPathResource("keys/public.pem").getInputStream());

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .build();

        JWKSet jwkSet = new JWKSet(rsaKey);
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(jwkSet);

        JwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(publicKey).build();
        this.jwtDecoder = jwtDecoder;

        JwtEncoder jwtEncoder = new NimbusJwtEncoder(jwkSource);
        this.jwtEncoder = jwtEncoder;
    }

    @Test
    public void testPasswordEncoder() {
        assertThat(passwordEncoder).isNotNull();
    }

    @Test
    public void testJwtDecoder() {
        assertThat(jwtDecoder).isNotNull();
    }

    @Test
    public void testJwtEncoder() throws Exception {
        assertThat(jwtEncoder).isNotNull();

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .subject("sample-id")
            .issuer("FPT")
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .build();

        JwtEncoderParameters parameters = JwtEncoderParameters.from(claims);
        Jwt jwt = jwtEncoder.encode(parameters);

        assertThat(jwt).isNotNull();
    }

    @Test
    public void testAuthenticationManager() throws Exception {
        assertThat(authenticationManager).isNotNull();
    }

    @Test
    public void testSecurityFilterChain() throws Exception {
        HttpSecurity http = mock(HttpSecurity.class);
        SecurityFilterChain filterChain = securityConfig.securityFilterChain(http);

        assertThat(filterChain).isNotNull();
    }
}
