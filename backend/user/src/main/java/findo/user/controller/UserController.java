package findo.user.controller;

import findo.user.dto.ShowDataDTO;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import findo.user.dto.AddBalanceDTO;
import findo.user.dto.ChangeNameDTO;
import findo.user.dto.ChangePasswordDTO;
import findo.user.service.impl.UserServiceImpl;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PutMapping("/update-profile")
    public Mono<ResponseEntity<String>> changeName(@AuthenticationPrincipal JwtAuthenticationToken principal,
            @Valid @RequestBody ChangeNameDTO changeNameDTO) {
        String userId = principal.getToken().getClaimAsString("sub"); // Extract user ID from JWT token's "sub" claim
        return userService.updateUserName(UUID.fromString(userId), changeNameDTO)
                .map(message -> ResponseEntity.ok(message));
    }

    @PutMapping("/change-password")
    public Mono<ResponseEntity<String>> changePassword(@AuthenticationPrincipal JwtAuthenticationToken principal,
            @Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        String userId = principal.getToken().getClaimAsString("sub"); // Extract user ID from JWT token's "sub" claim
        return userService.changePassword(UUID.fromString(userId), changePasswordDTO)
                .map(message -> ResponseEntity.ok(message));
    }

    @PutMapping("/top-up")
    public Mono<ResponseEntity<String>> addBalance(@AuthenticationPrincipal JwtAuthenticationToken principal,
            @Valid @RequestBody AddBalanceDTO addBalanceDTO) {
        String userId = principal.getToken().getClaimAsString("sub"); // Extract user ID from JWT token's "sub" claim
        return userService.addBalance(UUID.fromString(userId), addBalanceDTO)
                .map(message -> ResponseEntity.ok(message));
    }

    @GetMapping("/profile")
    public Mono<ResponseEntity<ShowDataDTO>> getUserData(@AuthenticationPrincipal JwtAuthenticationToken principal) {
        String userId = principal.getToken().getClaimAsString("sub"); // Extract user ID from JWT token's "sub" claim
        return userService.getUserDataById(UUID.fromString(userId))
                .map(ResponseEntity::ok);
    }
}
