package findo.schedule.dto;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDTO {
    private UUID id;
    private List<MovieDTO> movieIds;
    private List<StudioDTO> studioIds;
    private Timestamp showDate;
    private double price;
    private Timestamp createdTime;
    private Timestamp updatedTime;
    private String createdBy;
    private String updatedBy;
}
