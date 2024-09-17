package findo.user.service;

import java.util.UUID;

import findo.user.dto.AddBalanceDTO;
import findo.user.dto.ChangeNameDTO;
import findo.user.dto.ChangePasswordDTO;
import findo.user.dto.ShowDataDTO;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<String> updateUserName(UUID userId, ChangeNameDTO changeNameDTO);

    Mono<String> changePassword(UUID userId, ChangePasswordDTO changePasswordDTO);

    Mono<String> addBalance(UUID userId, AddBalanceDTO addBalanceDTO);

    Mono<ShowDataDTO> getUserDataById(UUID userId);
}
