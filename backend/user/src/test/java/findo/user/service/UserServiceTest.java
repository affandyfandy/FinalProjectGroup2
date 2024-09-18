package findo.user.service;

import findo.user.dto.AddBalanceDTO;
import findo.user.dto.ChangeNameDTO;
import findo.user.dto.ChangePasswordDTO;
import findo.user.dto.ShowDataDTO;
import findo.user.entity.EntityUser;
import findo.user.exception.UserNotFoundException;
import findo.user.repository.UserRepository;
import findo.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private EntityUser user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new EntityUser();
        user.setId(UUID.randomUUID());
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        user.setBalance(100.0);
    }

    @Test
    void testChangePassword_Success() {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO("password123", "newPassword123");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("newPassword123")).thenReturn("encodedNewPassword");

        Mono<String> result = userService.changePassword(user.getId(), changePasswordDTO);

        assertThat(result.block()).isEqualTo("Password Changed Successfully !");
        verify(userRepository).save(user);
        assertThat(user.getPassword()).isEqualTo("encodedNewPassword");
    }

    @Test
    void testChangePassword_UserNotFound() {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO("password123", "newPassword123");
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        // Directly call the method that might throw the exception
        Mono<Void> result = userService.changePassword(user.getId(), changePasswordDTO).then();

        assertThrows(UserNotFoundException.class, result::block);
    }

    @Test
    void testChangePassword_OldPasswordIncorrect() {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO("wrongPassword", "newPassword123");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", user.getPassword())).thenReturn(false);

        // Directly call the method that might throw the exception
        Mono<Void> result = userService.changePassword(user.getId(), changePasswordDTO).then();

        assertThrows(IllegalArgumentException.class, result::block);
    }

    @Test
    void testUpdateUserName_Success() {
        ChangeNameDTO changeNameDTO = new ChangeNameDTO("Jane Doe");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Mono<String> result = userService.updateUserName(user.getId(), changeNameDTO);

        assertThat(result.block()).isEqualTo("Name Changed Successfully");
        verify(userRepository).save(user);
        assertThat(user.getName()).isEqualTo("Jane Doe");
    }

    @Test
    void testUpdateUserName_UserNotFound() {
        ChangeNameDTO changeNameDTO = new ChangeNameDTO("Jane Doe");
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        Mono<Void> result = userService.updateUserName(user.getId(), changeNameDTO).then();

        assertThrows(UserNotFoundException.class, result::block);
    }

    @Test
    void testAddBalance_Success() {
        AddBalanceDTO addBalanceDTO = new AddBalanceDTO(50.0);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Mono<String> result = userService.addBalance(user.getId(), addBalanceDTO);

        assertThat(result.block()).isEqualTo("Top-Up Success !");
        verify(userRepository).save(user);
        assertThat(user.getBalance()).isEqualTo(150.0);
    }

    @Test
    void testAddBalance_UserNotFound() {
        AddBalanceDTO addBalanceDTO = new AddBalanceDTO(50.0);
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        Mono<Void> result = userService.addBalance(user.getId(), addBalanceDTO).then();

        assertThrows(UserNotFoundException.class, result::block);
    }

    @Test
    void testGetUserDataById_Success() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Mono<ShowDataDTO> result = userService.getUserDataById(user.getId());

        ShowDataDTO showDataDTO = result.block();
        assertThat(showDataDTO).isNotNull();
        assertThat(showDataDTO.getName()).isEqualTo("John Doe");
        assertThat(showDataDTO.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(showDataDTO.getBalance()).isEqualTo(100.0);
    }

    @Test
    void testGetUserDataById_UserNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        Mono<ShowDataDTO> result = userService.getUserDataById(user.getId());

        assertThrows(UserNotFoundException.class, result::block);
    }
}
