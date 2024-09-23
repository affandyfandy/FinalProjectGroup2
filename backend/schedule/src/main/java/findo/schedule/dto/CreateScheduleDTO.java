package findo.schedule.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateScheduleDTO {

    @NotEmpty(message = "Movie cannot be empty")
    private List<UUID> movieId;

    @NotEmpty(message = "Studio cannot be empty")
    private List<Integer> studioId;

    @NotNull(message = "Show date is required")
    @Future(message = "Show date must be in the future")
    private Timestamp showDate;

    @Positive(message = "Price must be greater than zero")
    private double price;
}