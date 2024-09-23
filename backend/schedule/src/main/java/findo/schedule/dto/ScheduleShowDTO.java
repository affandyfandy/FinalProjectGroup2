package findo.schedule.dto;

import java.sql.Timestamp;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleShowDTO {
    private UUID scheduleId;
    private Timestamp showDate;
    private String studioName;
    private double price;
}