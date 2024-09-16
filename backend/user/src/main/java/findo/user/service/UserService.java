package findo.user.service;

import java.util.Optional;
import java.util.UUID;

import findo.user.dto.AddBalanceDTO;
import findo.user.dto.ChangeNameDTO;
import findo.user.dto.ChangePasswordDTO;
import findo.user.dto.ShowDataDTO;
import findo.user.entity.EntityUser;

public interface UserService {
    Optional<EntityUser> updateUserName(UUID userId, ChangeNameDTO changeNameDTO);

    Optional<EntityUser> changePassword(UUID userId, ChangePasswordDTO changePasswordDTO);

    Optional<EntityUser> addBalance(UUID userId, AddBalanceDTO addBalanceDTO);

    ShowDataDTO getUserDataById(UUID userId);
}
