package findo.user.service.impl;

import java.util.Optional;
import java.util.UUID;

import findo.user.dto.ShowDataDTO;
import findo.user.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import findo.user.dto.AddBalanceDTO;
import findo.user.dto.ChangeNameDTO;
import findo.user.dto.ChangePasswordDTO;
import findo.user.entity.EntityUser;
import findo.user.repository.UserRepository;
import findo.user.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<EntityUser> updateUserName(UUID userId, ChangeNameDTO changeNameDTO) {
        return userRepository.findById(userId).map(user -> {
            user.setName(changeNameDTO.getNewName());
            return userRepository.save(user);
        });
    }

    public Optional<EntityUser> changePassword(UUID userId, ChangePasswordDTO changePasswordDTO) {
        return userRepository.findById(userId).map(user -> {
            if (passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
                return userRepository.save(user);
            }
            return null; // or throw an exception
        });
    }

    public Optional<EntityUser> addBalance(UUID userId, AddBalanceDTO addBalanceDTO) {
        return userRepository.findById(userId).map(user -> {
            user.setBalance(user.getBalance() + addBalanceDTO.getBalance());
            return userRepository.save(user);
        });
    }

    @Override
    public ShowDataDTO getUserDataById(UUID userId) {
        EntityUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        // Map EntityUser to ShowDataDTO
        ShowDataDTO showDataDTO = new ShowDataDTO();
        showDataDTO.setName(user.getName());
        showDataDTO.setEmail(user.getEmail());
        showDataDTO.setBalance(user.getBalance());
        return showDataDTO;
    }
}
