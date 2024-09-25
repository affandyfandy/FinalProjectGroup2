package findo.booking.dto;

import org.hibernate.validator.constraints.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDetailAdminClientDTO {
    private UUID movieId;
    private String movieTitle;
    private String movieSynopsis;
    private int movieYear;
    private int movieDuration;
    private String posterUrl;
    private List<ScheduleShowDTO> shows;
}
