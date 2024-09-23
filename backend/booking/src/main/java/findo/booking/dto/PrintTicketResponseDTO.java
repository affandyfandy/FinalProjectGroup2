package findo.booking.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrintTicketResponseDTO {
    private UUID bookingId;
    private boolean isPrinted;
    private String message;
    private ByteArrayInputStream pdfInputStream;
}