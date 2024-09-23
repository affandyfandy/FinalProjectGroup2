package findo.booking.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class CustomerBookingHistoryDTO {
    private UUID bookingId;
    private double totalAmount;
    private Timestamp createdTime;
    private Timestamp updatedTime;
    private boolean isPrinted;
}