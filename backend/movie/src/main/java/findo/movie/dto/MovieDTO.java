package findo.movie.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
    private UUID id;
    private String title;
    private String synopsis;
    private String duration;
    private String posterUrl;
    private int year;
}
