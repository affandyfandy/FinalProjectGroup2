package findo.auth.service;

import java.util.Optional;

import findo.auth.data.entity.User;
import findo.auth.dto.AuthDTO;

public interface AuthenticationService {
    User register(AuthDTO user);
    User findByEmail(String email);
    Optional<String> login(AuthDTO authRequest);
}
