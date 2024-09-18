package findo.booking.dto;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class BookingDetailDTO {
    private UUID bookingId;
    private List<Integer> seatIds;
    private List<UUID> scheduleIds;
    private double totalAmount;
    private boolean isPrinted;
    private String createdBy;
}
