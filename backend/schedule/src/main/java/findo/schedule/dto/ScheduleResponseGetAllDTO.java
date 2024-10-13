package findo.schedule.dto;

import java.sql.Timestamp;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleResponseGetAllDTO {
    private UUID id;
    private MovieDTO movie;
    private ScheduleShowDTO studio;
    private Timestamp showDate;
    private double price;
}
