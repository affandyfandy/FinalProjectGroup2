package findo.booking.repository;

import findo.booking.entity.Booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
    Page<Booking> findBookingsByUserIds(UUID userIds, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.userIds = :userId AND b.updatedTime BETWEEN :startOfDay AND :endOfDay")
    Page<Booking> findBookingsByUserIdsAndUpdatedTimeBetween(UUID userId, Timestamp startOfDay, Timestamp endOfDay,
            Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.updatedTime BETWEEN :startOfDay AND :endOfDay")
    Page<Booking> findAllBookingTimeBetween(Timestamp startOfDay, Timestamp endOfDay, Pageable pageable);
}
