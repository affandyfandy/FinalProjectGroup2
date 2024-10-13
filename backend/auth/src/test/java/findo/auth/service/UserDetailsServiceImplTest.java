package findo.auth.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import findo.auth.data.entity.User;
import findo.auth.data.repository.UserRepository;
import findo.auth.dto.UserDetailsDTO;
import findo.auth.service.impl.UserDetailsServiceImpl;

public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_success() {
        String email = "johndoe@example.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("password");

        UserDetailsDTO userDetailsDTO = new UserDetailsDTO(user);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername(email);

        assertNotNull(result);
        assertEquals(email, result.getUsername());
        assertTrue(result instanceof UserDetailsDTO);

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testLoadUserByUsername_userNotFound() {
        String email = "johndoe@example.com";
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(email);
        });

        assertEquals("User not found", exception.getMessage());

        verify(userRepository, times(1)).findByEmail(email);
    }
}
