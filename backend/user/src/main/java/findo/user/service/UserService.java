package findo.user.service;

import java.util.UUID;

import findo.user.dto.*;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<ShowDataDTO> updateUserName(UUID userId, ChangeNameDTO changeNameDTO);

    Mono<ChangePasswordResponseDTO> changePassword(UUID userId, ChangePasswordDTO changePasswordDTO);

    Mono<AddBalanceDTO> addBalance(UUID userId, AddBalanceDTO addBalanceDTO);

    Mono<ShowDataDTO> getUserDataById(UUID userId);
    Mono<UpdateBalanceDTO> updateBalance(UUID userId, double newBalance);
}
