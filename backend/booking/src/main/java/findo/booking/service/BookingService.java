package findo.booking.service;

import findo.booking.dto.*;
import findo.booking.entity.Booking;
import reactor.core.publisher.Mono;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookingService {
    Page<BookingResponseDTO> getAllBookings(Pageable pageable);

    Mono<Page<CustomerBookingHistoryDTO>> getBookingHistoryByUser(UUID userId, Pageable pageable);

    BookingDetailDTO getBookingDetail(UUID bookingId);

    Mono<Booking> createBooking(CreateBookingDTO request, UUID userId, String token, String email);

    Mono<PrintTicketResponseDTO> printTicket(UUID bookingId, String email);

}
