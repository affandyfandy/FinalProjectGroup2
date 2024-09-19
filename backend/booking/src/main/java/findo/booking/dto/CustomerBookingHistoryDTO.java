package findo.booking.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class CustomerBookingHistoryDTO {
    private UUID bookingId;
    private double totalAmount;
    private LocalDate createdTime;
    private boolean isPrinted;
}