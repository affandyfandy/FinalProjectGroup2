package findo.user.controller;

import findo.user.dto.ShowDataDTO;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import findo.user.dto.AddBalanceDTO;
import findo.user.dto.ChangeNameDTO;
import findo.user.dto.ChangePasswordDTO;
import findo.user.dto.ChangePasswordResponseDTO;
import findo.user.service.impl.UserServiceImpl;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PutMapping("/update-profile")
    public Mono<ResponseEntity<ShowDataDTO>> changeName(@AuthenticationPrincipal JwtAuthenticationToken principal,
            @Valid @RequestBody ChangeNameDTO changeNameDTO) {
        String userId = principal.getToken().getClaimAsString("sub"); // Extract user ID from JWT token's "sub" claim
        return userService.updateUserName(UUID.fromString(userId), changeNameDTO)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/change-password")
    public Mono<ResponseEntity<ChangePasswordResponseDTO>> changePassword(@AuthenticationPrincipal JwtAuthenticationToken principal,
            @Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        String userId = principal.getToken().getClaimAsString("sub"); // Extract user ID from JWT token's "sub" claim
        return userService.changePassword(UUID.fromString(userId), changePasswordDTO)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/top-up")
    public Mono<ResponseEntity<AddBalanceDTO>> addBalance(@AuthenticationPrincipal JwtAuthenticationToken principal,
            @Valid @RequestBody AddBalanceDTO addBalanceDTO) {
        String userId = principal.getToken().getClaimAsString("sub"); // Extract user ID from JWT token's "sub" claim
        return userService.addBalance(UUID.fromString(userId), addBalanceDTO)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/profile")
    public Mono<ResponseEntity<ShowDataDTO>> getUserData(@AuthenticationPrincipal JwtAuthenticationToken principal) {
        String userId = principal.getToken().getClaimAsString("sub"); // Extract user ID from JWT token's "sub" claim
        return userService.getUserDataById(UUID.fromString(userId))
                .map(ResponseEntity::ok);
    }
}
