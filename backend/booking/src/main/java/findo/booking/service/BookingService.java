package findo.booking.service;

import findo.booking.dto.BookingDetailDTO;
import findo.booking.dto.BookingResponseDTO;
import findo.booking.dto.CreateBookingDTO;
import findo.booking.dto.CustomerBookingHistoryDTO;
import findo.booking.entity.Booking;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface BookingService {
    List<BookingResponseDTO> getAllBookings();

    Mono<List<CustomerBookingHistoryDTO>> getBookingHistoryByUser(UUID userId);

    BookingDetailDTO getBookingDetail(UUID bookingId);

    Booking createBooking(CreateBookingDTO request, UUID userId);
}
