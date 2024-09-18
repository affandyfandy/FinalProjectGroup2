package findo.user.controller;

import findo.user.dto.AddBalanceDTO;
import findo.user.dto.ChangeNameDTO;
import findo.user.dto.ChangePasswordDTO;
import findo.user.dto.ShowDataDTO;
import findo.user.exception.UserNotFoundException;
import findo.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    private JwtAuthenticationToken principal;
    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsString("sub")).thenReturn(userId.toString());
        principal = new JwtAuthenticationToken(jwt);
    }

    @Test
    void testChangeName_Success() {
        ChangeNameDTO changeNameDTO = new ChangeNameDTO("Jane Doe");
        when(userService.updateUserName(userId, changeNameDTO)).thenReturn(Mono.just("Name Changed Successfully"));

        Mono<ResponseEntity<String>> response = userController.changeName(principal, changeNameDTO);

        ResponseEntity<String> result = response.block();
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo("Name Changed Successfully");
    }

    @Test
    void testChangeName_UserNotFound() {
        ChangeNameDTO changeNameDTO = new ChangeNameDTO("Jane Doe");
        when(userService.updateUserName(userId, changeNameDTO))
                .thenReturn(Mono.error(new UserNotFoundException("User not found")));

        Mono<ResponseEntity<String>> response = userController.changeName(principal, changeNameDTO);

        StepVerifier.create(response)
                .expectErrorMatches(throwable -> throwable instanceof UserNotFoundException
                        && throwable.getMessage().equals("User not found"))
                .verify();
    }
    @Test
    void testChangePassword_Success() {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO("oldPassword", "newPassword");
        when(userService.changePassword(userId, changePasswordDTO)).thenReturn(Mono.just("Password Changed Successfully"));

        Mono<ResponseEntity<String>> response = userController.changePassword(principal, changePasswordDTO);

        ResponseEntity<String> result = response.block();
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo("Password Changed Successfully");
    }

    @Test
    void testChangePassword_UserNotFound() {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO("oldPassword", "newPassword");
        when(userService.changePassword(userId, changePasswordDTO))
                .thenReturn(Mono.error(new UserNotFoundException("User not found")));

        Mono<ResponseEntity<String>> response = userController.changePassword(principal, changePasswordDTO);

        StepVerifier.create(response)
                .expectErrorMatches(throwable -> throwable instanceof UserNotFoundException
                        && throwable.getMessage().equals("User not found"))
                .verify();
    }

    @Test
    void testAddBalance_Success() {
        AddBalanceDTO addBalanceDTO = new AddBalanceDTO(50.0);
        when(userService.addBalance(userId, addBalanceDTO)).thenReturn(Mono.just("Top-Up Success !"));

        Mono<ResponseEntity<String>> response = userController.addBalance(principal, addBalanceDTO);

        ResponseEntity<String> result = response.block();
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo("Top-Up Success !");
    }

    @Test
    void testAddBalance_UserNotFound() {
        AddBalanceDTO addBalanceDTO = new AddBalanceDTO(50.0);
        when(userService.addBalance(userId, addBalanceDTO))
                .thenReturn(Mono.error(new UserNotFoundException("User not found")));

        Mono<ResponseEntity<String>> response = userController.addBalance(principal, addBalanceDTO);

        StepVerifier.create(response)
                .expectErrorMatches(throwable -> throwable instanceof UserNotFoundException
                        && throwable.getMessage().equals("User not found"))
                .verify();
    }

    @Test
    void testGetUserData_Success() {
        ShowDataDTO showDataDTO = new ShowDataDTO("John Doe", "john.doe@example.com", 100.0);
        when(userService.getUserDataById(userId)).thenReturn(Mono.just(showDataDTO));

        Mono<ResponseEntity<ShowDataDTO>> response = userController.getUserData(principal);

        ResponseEntity<ShowDataDTO> result = response.block();
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(showDataDTO);
    }

    @Test
    void testGetUserData_UserNotFound() {
        when(userService.getUserDataById(userId))
                .thenReturn(Mono.error(new UserNotFoundException("User not found")));

        Mono<ResponseEntity<ShowDataDTO>> response = userController.getUserData(principal);

        StepVerifier.create(response)
                .expectErrorMatches(throwable -> throwable instanceof UserNotFoundException
                        && throwable.getMessage().equals("User not found"))
                .verify();
    }
}
