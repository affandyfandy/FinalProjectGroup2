package findo.schedule.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO {
    private UUID id;
    private String title;
    private String synopsis;
    private int duration;
    private String posterUrl;
    private int year;
}
