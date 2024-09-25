package findo.booking.service;

import findo.booking.dto.*;
import findo.booking.entity.Booking;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookingService {
    Mono<Page<BookingResponseDTO>> getAllBookings(Pageable pageable, String token);
    // Page<BookingResponseDTO> getAllBookings(Pageable pageable, String token);

    Mono<Page<BookingResponseDTO>> getBookingHistoryByUser(UUID userId, Pageable pageable, String token);

    BookingDetailDTO getBookingDetail(UUID bookingId);

    Mono<Booking> createBooking(CreateBookingDTO request, UUID userId, String token, String email);

    Mono<PrintTicketResponseDTO> printTicket(UUID bookingId, String email);

    BookingSeatsDTO getAllSeatIds(UUID scheduleId);

}
