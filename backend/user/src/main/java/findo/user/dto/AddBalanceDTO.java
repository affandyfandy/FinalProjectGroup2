package findo.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddBalanceDTO {
    @NotNull
    private double balance;
}
