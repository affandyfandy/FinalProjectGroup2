package findo.booking.controller;

import findo.booking.dto.BookingDetailDTO;
import findo.booking.dto.BookingResponseDTO;
import findo.booking.dto.CreateBookingDTO;
import findo.booking.dto.CustomerBookingHistoryDTO;
import findo.booking.entity.Booking;
import findo.booking.service.impl.BookingServiceImpl;
import reactor.core.publisher.Mono;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingServiceImpl bookingService;

    public BookingController(BookingServiceImpl bookingService) {
        this.bookingService = bookingService;
    }

    // Show All Booking History (Admin)
    @GetMapping("/admin/history")
    public List<BookingResponseDTO> getAllBookingHistory() {
        return bookingService.getAllBookings();
    }

    // Show Booking History (Customer)
    @GetMapping("/booking-history")
    public Mono<ResponseEntity<List<CustomerBookingHistoryDTO>>> getBookingHistory(
            @AuthenticationPrincipal JwtAuthenticationToken principal) {

        // Extract user ID from JWT token's "sub" claim
        String userId = principal.getToken().getClaimAsString("sub");

        return bookingService.getBookingHistoryByUser(UUID.fromString(userId))
                .map(ResponseEntity::ok);
    }

    // Show Booking Detail (Customer)
    @GetMapping("/customer/history/detail/{bookingId}")
    public BookingDetailDTO getBookingDetail(@PathVariable UUID bookingId) {
        return bookingService.getBookingDetail(bookingId);
    }

    @PostMapping("/customer/book")
    public Booking createBooking(@AuthenticationPrincipal JwtAuthenticationToken principal,
            @RequestBody CreateBookingDTO request) {

        String userId = principal.getToken().getClaimAsString("sub");

        return bookingService.createBooking(request, UUID.fromString(userId));
    }
}