package findo.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShowDataDTO {
    @NotBlank(message="Your name is required")
    private String name;

    @NotBlank(message="Email is required")
    @Email(message="Email should be valid")
    private String email;

    @NotNull
    private double balance;
}
