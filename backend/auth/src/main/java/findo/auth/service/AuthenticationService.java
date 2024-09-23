package findo.auth.service;

import findo.auth.data.entity.User;
import findo.auth.dto.LoginDTO;
import findo.auth.dto.LoginResponseDTO;
import findo.auth.dto.RegisterDTO;

public interface AuthenticationService {
    User register(RegisterDTO user);
    User findByEmail(String email);
    LoginResponseDTO login(LoginDTO authRequest);
}
