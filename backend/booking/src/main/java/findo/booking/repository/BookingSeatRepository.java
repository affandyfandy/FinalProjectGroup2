package findo.booking.repository;

import findo.booking.entity.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Integer> {
    List<BookingSeat> findByBookingId(UUID booking_id);
}
