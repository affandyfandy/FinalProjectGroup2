package findo.schedule.entity;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "schedules")
public class Schedule {

    @Id
    private Integer id;

    @Column(nullable = false)
    private UUID movieId;
    private UUID studioId;
    private Double price;
    private LocalDate showTime;
    private LocalDate createdTime;
    private LocalDate updatedTime;
    private String createdBy;
    private String updatedBy;
}
