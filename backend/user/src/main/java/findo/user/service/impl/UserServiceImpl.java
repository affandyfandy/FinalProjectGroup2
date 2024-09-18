package findo.user.service.impl;

import java.util.UUID;

import findo.user.dto.ShowDataDTO;
import findo.user.exception.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import findo.user.dto.AddBalanceDTO;
import findo.user.dto.ChangeNameDTO;
import findo.user.dto.ChangePasswordDTO;
import findo.user.dto.ChangePasswordResponseDTO;
import findo.user.repository.UserRepository;
import findo.user.service.UserService;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND_MESSAGE = "User not found with ID: ";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<ChangePasswordResponseDTO> changePassword(UUID userId, ChangePasswordDTO changePasswordDTO) {
        return Mono.justOrEmpty(userRepository.findById(userId))
                .switchIfEmpty(Mono.error(new UserNotFoundException(USER_NOT_FOUND_MESSAGE + userId)))
                .flatMap(user -> {
                    if (passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
                        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
                        userRepository.save(user);
                        return Mono.just(new ChangePasswordResponseDTO("Password Changed Successfully!"));
                    } else {
                        return Mono.error(new IllegalArgumentException("Old password is incorrect"));
                    }
                });
    }

    @Override
    public Mono<ShowDataDTO> updateUserName(UUID userId, ChangeNameDTO changeNameDTO) {
        return Mono.justOrEmpty(userRepository.findById(userId))
                .switchIfEmpty(Mono.error(new UserNotFoundException(USER_NOT_FOUND_MESSAGE + userId)))
                .flatMap(user -> {
                    user.setName(changeNameDTO.getNewName());
                    userRepository.save(user);
                    return Mono.just(new ShowDataDTO(user.getName(), user.getEmail(), user.getBalance()));
                });
    }

    @Override
    public Mono<AddBalanceDTO> addBalance(UUID userId, AddBalanceDTO addBalanceDTO) {
        return Mono.justOrEmpty(userRepository.findById(userId))
                .switchIfEmpty(Mono.error(new UserNotFoundException(USER_NOT_FOUND_MESSAGE + userId)))
                .flatMap(user -> {
                    user.setBalance(user.getBalance() + addBalanceDTO.getBalance());
                    userRepository.save(user);
                    return Mono.just(new AddBalanceDTO(user.getBalance()));
                });
    }

    @Override
    public Mono<ShowDataDTO> getUserDataById(UUID userId) {
        return Mono.justOrEmpty(userRepository.findById(userId))
                .switchIfEmpty(Mono.error(new UserNotFoundException(USER_NOT_FOUND_MESSAGE + userId)))
                .map(user -> new ShowDataDTO(user.getName(), user.getEmail(), user.getBalance()));
    }
}
