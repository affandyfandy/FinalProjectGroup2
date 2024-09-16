package findo.auth.service;

import java.util.Optional;

import findo.auth.data.entity.User;
import findo.auth.dto.LoginDTO;
import findo.auth.dto.RegisterDTO;

public interface AuthenticationService {
    User register(RegisterDTO user);
    User findByEmail(String email);
    Optional<String> login(LoginDTO authRequest);
}
