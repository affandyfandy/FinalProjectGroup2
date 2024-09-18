package findo.booking.dto;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class CreateBookingDTO {
    private List<Integer> seatIds;
    private List<UUID> scheduleIds;
    private UUID userId;
    private double totalAmount;
}