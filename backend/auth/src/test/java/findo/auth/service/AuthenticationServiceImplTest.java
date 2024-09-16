package findo.auth.service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import findo.auth.data.entity.User;
import findo.auth.data.repository.UserRepository;
import findo.auth.dto.LoginDTO;
import findo.auth.dto.RegisterDTO;
import findo.auth.dto.UserDetailsDTO;
import findo.auth.service.impl.AuthenticationServiceImpl;

class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_success() {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setName("John Doe");
        registerDTO.setEmail("johndoe@example.com");
        registerDTO.setPassword("password");

        User savedUser = new User();
        savedUser.setId(UUID.randomUUID());
        savedUser.setName("John Doe");
        savedUser.setEmail("johndoe@example.com");
        savedUser.setRole("ROLE_CUSTOMER");
        savedUser.setPassword("encodedPassword");
        savedUser.setCreatedBy("johndoe@example.com");
        savedUser.setUpdatedBy("johndoe@example.com");
        savedUser.setCreatedTime(new Date());
        savedUser.setUpdatedTime(new Date());

        when(passwordEncoder.encode(registerDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = authenticationService.register(registerDTO);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("johndoe@example.com", result.getEmail());
        assertEquals("ROLE_CUSTOMER", result.getRole());
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testFindByEmail_userExists() {
        // Arrange
        String email = "johndoe@example.com";
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        User result = authenticationService.findByEmail(email);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testFindByEmail_userDoesNotExist() {
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        User result = authenticationService.findByEmail(email);

        assertNull(result);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testLogin_success() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("johndoe@example.com");
        loginDTO.setPassword("password");

        User savedUser = new User();
        savedUser.setId(UUID.randomUUID());
        savedUser.setName("John Doe");
        savedUser.setEmail("johndoe@example.com");
        savedUser.setRole("ROLE_CUSTOMER");
        savedUser.setPassword("encodedPassword");
        savedUser.setCreatedBy("johndoe@example.com");
        savedUser.setUpdatedBy("johndoe@example.com");
        savedUser.setCreatedTime(new Date());
        savedUser.setUpdatedTime(new Date());

        UserDetailsDTO userDetailsDTO = new UserDetailsDTO(savedUser);

        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        JwtEncoder jwtEncoder = mock(JwtEncoder.class);
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        AuthenticationServiceImpl authenticationService = new AuthenticationServiceImpl(
            userRepository,
            passwordEncoder,
            jwtEncoder,
            authenticationManager
        );

        when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.of(savedUser));
        when(passwordEncoder.matches(loginDTO.getPassword(), savedUser.getPassword())).thenReturn(true);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetailsDTO);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        Jwt jwt = mock(Jwt.class);
        when(jwt.getTokenValue()).thenReturn("mockedJwtToken");
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

        Optional<String> result = authenticationService.login(loginDTO);

        assertTrue(result.isPresent());
        assertEquals("mockedJwtToken", result.get());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtEncoder, times(1)).encode(any(JwtEncoderParameters.class));
    }

    @Test
    void testLogin_invalidCredentials() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("wrong@example.com");
        loginDTO.setPassword("wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Invalid credentials") {});

        assertThrows(AuthenticationException.class, () -> authenticationService.login(loginDTO));
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
