package findo.booking.service;

import findo.booking.dto.*;
import findo.booking.entity.Booking;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookingService {
    Mono<Page<BookingResponseDTO>> getAllBookings(Pageable pageable, LocalDate date, String token);
    // Page<BookingResponseDTO> getAllBookings(Pageable pageable, String token);

    Mono<Page<BookingResponseDTO>> getBookingHistoryByUser(UUID userId, Pageable pageable, LocalDate date,
            String token);

    BookingDetailDTO getBookingDetail(UUID bookingId);

    Mono<Booking> createBooking(CreateBookingDTO request, UUID userId, String token, String email);

    Mono<PrintTicketResponseDTO> printTicket(UUID bookingId, String email);

    BookingSeatsDTO getAllSeatIds(UUID scheduleId);

    Mono<ScheduleDetailsAdmin> getBookingDetails(UUID scheduleId, String token);

}
