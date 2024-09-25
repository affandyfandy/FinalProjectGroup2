package findo.booking.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDetailsAdmin {
    private UUID bookingId;
    private double totalAmount;
    private UUID custId;
    private String custName;
    private List<MovieDetailDTO> movies;
    private List<StudioDetailDTO> studios;
    private List<ScheduleDetailDTO> schedules;
    private List<SeatDetailDTO> seats;
}
