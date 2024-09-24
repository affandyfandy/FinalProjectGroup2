package findo.booking.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
public class BookingResponseDTO {
    private UUID id;
    private List<ScheduleMovieDTO> scheduleIds;
    private double totalAmount;
    private Boolean isPrinted;
    private Timestamp createdTime;
    private Timestamp updatedTime;
    private String createdBy;
    private String updatedBy;
}