package findo.user.service;

import java.util.UUID;

import findo.user.dto.AddBalanceDTO;
import findo.user.dto.ChangeNameDTO;
import findo.user.dto.ChangePasswordDTO;
import findo.user.dto.ChangePasswordResponseDTO;
import findo.user.dto.ShowDataDTO;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<ShowDataDTO> updateUserName(UUID userId, ChangeNameDTO changeNameDTO);

    Mono<ChangePasswordResponseDTO> changePassword(UUID userId, ChangePasswordDTO changePasswordDTO);

    Mono<AddBalanceDTO> addBalance(UUID userId, AddBalanceDTO addBalanceDTO);

    Mono<ShowDataDTO> getUserDataById(UUID userId);
}
