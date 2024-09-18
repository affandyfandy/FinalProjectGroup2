package findo.auth.controller;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import findo.auth.data.entity.User;
import findo.auth.dto.LoginDTO;
import findo.auth.dto.RegisterDTO;
import findo.auth.service.AuthenticationService;

@EnableWebMvc
public class AuthenticationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void register_ShouldReturnSuccessMessage() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setEmail("johndoe@example.com");
        registerDTO.setPassword("password");
        registerDTO.setName("John Doe");

        User user = new User();
        user.setEmail("johndoe@example.com");

        when(authenticationService.register(any(RegisterDTO.class))).thenReturn(user);

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(registerDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully: " + user.getEmail()));
    }

    @Test
    void login_ShouldReturnToken() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("johndoe@example.com");
        loginDTO.setPassword("password");

        String token = "jwtToken";
        when(authenticationService.login(any(LoginDTO.class))).thenReturn(Optional.of(token));

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(token));
    }

    @Test
    void login_ShouldReturnUnauthorizedWhenInvalidCredentials() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("johndoe@example.com");
        loginDTO.setPassword("wrongPassword");

        when(authenticationService.login(any(LoginDTO.class))).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(loginDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }

    private String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
