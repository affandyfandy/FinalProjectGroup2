package findo.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordDTO {
    @NotBlank(message="Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String oldPassword;

    @NotBlank(message="Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String newPassword;
}