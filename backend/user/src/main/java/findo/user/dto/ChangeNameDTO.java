package findo.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeNameDTO {
    @NotBlank(message="Your name is required")
    private String newName;
}
