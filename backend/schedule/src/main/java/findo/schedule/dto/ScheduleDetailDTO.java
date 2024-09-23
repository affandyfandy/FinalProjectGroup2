package findo.schedule.dto;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDetailDTO {
    private UUID movieId;
    private String movieTitle;
    private String movieSynopsis;
    private int movieYear;
    private int movieDuration;
    private String posterUrl;
    private List<ScheduleShowDTO> shows; // List of shows containing details
}
