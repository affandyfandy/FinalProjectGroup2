package findo.movie.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieSaveDTO {
    @NotBlank(message="Title is Required")
    private String title;

    @NotBlank(message="Synopsis is Required")
    private String synopsis;

    @NotBlank(message="Image is Required")
    private String posterUrl;

    @NotNull(message="Year is Required")
    private int year;
}
