package findo.schedule.dto;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleStudioSeatDTO {
    private UUID scheduleId;
    private double price;
    private Timestamp showDate;
    private List<StudioDTO> studio;
    private List<SeatDTO> seats;
}
