package findo.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponseDTO {
    private UUID id;
    private List<UUID> movieId;
    private List<Integer> studioId;
    private Timestamp showDate;
    private double price;
    private Timestamp createdTime;
    private Timestamp updatedTime;
    private String createdBy;
    private String updatedBy;
}