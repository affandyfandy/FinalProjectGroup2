package findo.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
    private UUID id;
    private String title;
    private String synopsis;
    private int year;
    private int duration;
    private String posterUrl;
}