package findo.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableScheduleDTO {
    private UUID movieId;
    private String movieTitle;
    private String movieSynopsis;
    private int movieYear;
    private int movieDuration;
    private String studioName;
    private String posterUrl;
    private List<Timestamp> showDates;
}