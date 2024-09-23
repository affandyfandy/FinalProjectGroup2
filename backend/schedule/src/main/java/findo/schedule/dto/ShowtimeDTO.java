package findo.schedule.dto;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowtimeDTO {
    private Timestamp showDate;
    private String studioName;
    private double price;
}
