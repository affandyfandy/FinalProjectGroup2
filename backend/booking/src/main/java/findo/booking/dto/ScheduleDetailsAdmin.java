package findo.booking.dto;

import java.sql.Timestamp;
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
    private Timestamp createdAt;
    private double price;
    private double totalAmount;
    private UUID custId;
    private String custName;
    private Timestamp showDate;
    private List<MovieDetailDTO> movies;
    private List<StudioDetailDTO> studios;
    private List<SeatDTO> seats;
}
