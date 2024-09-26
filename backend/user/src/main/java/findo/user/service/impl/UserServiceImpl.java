package findo.user.service.impl;

import java.util.UUID;

import findo.user.core.AppConstant;
import findo.user.dto.*;
import findo.user.exception.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import findo.user.repository.UserRepository;
import findo.user.service.UserService;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<ChangePasswordResponseDTO> changePassword(UUID userId, ChangePasswordDTO changePasswordDTO) {
        return Mono.justOrEmpty(userRepository.findById(userId))
                .switchIfEmpty(Mono.error(new UserNotFoundException(AppConstant.UserNotFoundMsg.getValue() + userId)))
                .flatMap(user -> {
                    if (passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
                        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
                        userRepository.save(user);
                        return Mono
                                .just(new ChangePasswordResponseDTO(AppConstant.UserPasswordChangeSuccess.getValue()));
                    } else {
                        return Mono.error(new IllegalArgumentException(AppConstant.UserPasswordIncorect.getValue()));
                    }
                });
    }

    @Override
    public Mono<ShowDataDTO> updateUserName(UUID userId, ChangeNameDTO changeNameDTO) {
        return Mono.justOrEmpty(userRepository.findById(userId))
                .switchIfEmpty(Mono.error(new UserNotFoundException(AppConstant.UserNotFoundMsg.getValue() + userId)))
                .flatMap(user -> {
                    user.setName(changeNameDTO.getNewName());
                    userRepository.save(user);
                    return Mono.just(new ShowDataDTO(user.getName(), user.getEmail(), user.getBalance()));
                });
    }

    @Override
    public Mono<AddBalanceDTO> addBalance(UUID userId, AddBalanceDTO addBalanceDTO) {
        return Mono.justOrEmpty(userRepository.findById(userId))
                .switchIfEmpty(Mono.error(new UserNotFoundException(AppConstant.UserNotFoundMsg.getValue() + userId)))
                .flatMap(user -> {
                    user.setBalance(user.getBalance() + addBalanceDTO.getBalance());
                    userRepository.save(user);
                    return Mono.just(new AddBalanceDTO(user.getBalance()));
                });
    }

    @Override
    public Mono<ShowDataDTO> getUserDataById(UUID userId) {
        return Mono.justOrEmpty(userRepository.findById(userId))
                .switchIfEmpty(Mono.error(new UserNotFoundException(AppConstant.UserNotFoundMsg.getValue() + userId)))
                .map(user -> new ShowDataDTO(user.getName(), user.getEmail(), user.getBalance()));
    }

    @Override
    public Mono<UpdateBalanceDTO> updateBalance(UUID userId, double newBalance) {
        return Mono.justOrEmpty(userRepository.findById(userId))
                .switchIfEmpty(Mono.error(new UserNotFoundException(AppConstant.UserNotFoundMsg.getValue() + userId)))
                .publishOn(Schedulers.boundedElastic())
                .flatMap(user -> {
                    user.setBalance(newBalance);
                    userRepository.save(user);
                    return Mono.just(new UpdateBalanceDTO(user.getBalance()));
                });
    }
}
