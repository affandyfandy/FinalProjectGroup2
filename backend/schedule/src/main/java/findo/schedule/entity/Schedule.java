package findo.schedule.entity;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "schedule_movie", joinColumns = @JoinColumn(name = "schedule_id"))
    @Column(name = "movie_id", nullable = false)
    private List<UUID> movieId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "schedule_studio", joinColumns = @JoinColumn(name = "schedule_id"))
    @Column(name = "studio_id", nullable = false)
    private List<Integer> studioId;

    @Column(name = "show_date", nullable = false)
    private Timestamp showDate;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "created_time", updatable = false)
    private Timestamp createdTime;

    @Column(name = "updated_time")
    private Timestamp updatedTime;

    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;
}
