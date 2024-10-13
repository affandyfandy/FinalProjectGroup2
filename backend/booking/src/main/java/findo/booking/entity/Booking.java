package findo.booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bookings")
@EntityListeners(AuditingEntityListener.class)
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id")
    private UUID userIds;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "booking_schedule", joinColumns = @JoinColumn(name = "booking_id"))
    @Column(name = "schedule_id")
    private List<UUID> scheduleIds;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<BookingSeat> bookingSeats;

    @Column(nullable = false)
    private double totalAmount;

    @Column(nullable = false)
    private Boolean isPrinted;

    @CreatedDate
    @Column(name = "created_time", updatable = false)
    private Timestamp createdTime;

    @LastModifiedDate
    @Column(name = "updated_time")
    private Timestamp updatedTime;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;
}
