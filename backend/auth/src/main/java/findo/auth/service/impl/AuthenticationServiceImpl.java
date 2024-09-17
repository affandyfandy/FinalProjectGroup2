package findo.auth.service.impl;

import java.time.Instant;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import findo.auth.data.entity.User;
import findo.auth.data.repository.UserRepository;
import findo.auth.dto.LoginDTO;
import findo.auth.dto.LoginResponseDTO;
import findo.auth.dto.RegisterDTO;
import findo.auth.dto.UserDetailsDTO;
import findo.auth.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    
    final private UserRepository userRepository;
    final private PasswordEncoder passwordEncoder;
    final private JwtEncoder jwtEncoder;
    final private AuthenticationManager authenticationManager;
    
    @Override
    public User register(RegisterDTO user) {
        User userData = new User();
        userData.setName(user.getName());
        userData.setEmail(user.getEmail());
        userData.setRole("ROLE_CUSTOMER");
        userData.setPassword(passwordEncoder.encode(user.getPassword()));
        userData.setCreatedBy(user.getEmail());
        userData.setUpdatedBy(user.getEmail());
        userData.setCreatedTime(new Date());
        userData.setUpdatedTime(new Date());

        return userRepository.save(userData);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public LoginResponseDTO login(@Valid LoginDTO authRequest) {
        try {
            // Get user authentication
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            // Get UserDetailsDTO after success authentication
            UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();
            User user = userDetails.getUser();

            // Claim JWT
            Instant now = Instant.now();
            long expiry = 604800L; // Token valid for 7 days

            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("FPT")
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(expiry))
                    .subject(user.getId().toString())  // Store User ID as a subject
                    .claim("email", user.getEmail())  // Store email as a claim
                    .claim("roles", userDetails.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority).toList())
                    .build();

            // Encode JWT using claim
            String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

            return new LoginResponseDTO(token, user.getRole());
        } catch (AuthenticationException e) {
            throw e;
        }
    }    
}
