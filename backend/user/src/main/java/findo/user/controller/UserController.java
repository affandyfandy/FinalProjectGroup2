package findo.user.controller;

import findo.user.dto.ShowDataDTO;
import findo.user.exception.UserNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import findo.user.dto.AddBalanceDTO;
import findo.user.dto.ChangeNameDTO;
import findo.user.dto.ChangePasswordDTO;
import findo.user.entity.EntityUser;
import findo.user.service.impl.UserServiceImpl;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PutMapping("/profile/{id}/name")
    public ResponseEntity<EntityUser> updateUserName(@PathVariable UUID id, @Valid @RequestBody ChangeNameDTO changeNameDTO) {
        return userService.updateUserName(id, changeNameDTO)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    @PutMapping("/profile/{id}/change-password")
    public ResponseEntity<EntityUser> changePassword(@PathVariable UUID id, @Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        return userService.changePassword(id, changePasswordDTO)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    @PutMapping("/profile/{id}/top-up")
    public ResponseEntity<EntityUser> addBalance(@PathVariable UUID id, @Valid @RequestBody AddBalanceDTO addBalanceDTO) {
        return userService.addBalance(id, addBalanceDTO)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<ShowDataDTO> getUserData(@PathVariable UUID id) {
        ShowDataDTO showDataDTO = userService.getUserDataById(id);
        return ResponseEntity.ok(showDataDTO);
    }
}
