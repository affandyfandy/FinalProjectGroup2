package findo.booking.dto;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class CreateBookingDTO {
    private List<Integer> seatIds;
    private List<UUID> scheduleIds;
    private double totalAmount;
}