package findo.booking.repository;

import findo.booking.entity.Booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
    Page<Booking> findBookingsByUserIds(UUID userIds, Pageable pageable);
}
