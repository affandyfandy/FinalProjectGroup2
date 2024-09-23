package findo.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieScheduleDTO {
    private String movieTitle;
    private String movieSynopsis;
    private int movieYear;
    private double price;
}
