package findo.booking.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class BookingResponseDTO {
    private UUID id;
    private double totalAmount;
    private Boolean isPrinted;
    private LocalDate createdTime;
    private LocalDate updatedTime;
    private String createdBy;
    private String updatedBy;
}