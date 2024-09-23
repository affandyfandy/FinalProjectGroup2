package findo.schedule.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieShowtimeDTO {
    private String movieTitle;
    private String movieSynopsis;
    private int movieYear;
    private int movieDuration;
    private List<ShowtimeDTO> showtimes;
}
