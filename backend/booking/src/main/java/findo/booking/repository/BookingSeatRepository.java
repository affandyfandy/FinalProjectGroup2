package findo.booking.repository;

import findo.booking.entity.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Integer> {
    List<BookingSeat> findByBookingId(UUID booking_id);

    @Query("SELECT bs FROM BookingSeat bs LEFT JOIN FETCH bs.seatIds WHERE bs.booking.id = :bookingId")
    List<BookingSeat> findByBookingIdWithSeatIds(@Param("bookingId") UUID bookingId);

    List<BookingSeat> findByBooking_ScheduleIdsContaining(UUID scheduleId);
}
