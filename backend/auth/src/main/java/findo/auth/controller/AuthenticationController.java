package findo.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import findo.auth.data.entity.User;
import findo.auth.dto.LoginDTO;
import findo.auth.dto.LoginResponseDTO;
import findo.auth.dto.RegisterDTO;
import findo.auth.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@Validated
public class AuthenticationController {

    final private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDTO user) {
        User response = authenticationService.register(user);
        return ResponseEntity.ok("User registered successfully: " + response.getEmail());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginDTO authRequest) {
        LoginResponseDTO response = authenticationService.login(authRequest);
        return ResponseEntity.ok(response);
    }
}
