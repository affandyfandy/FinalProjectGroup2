package findo.booking.controller;

import findo.booking.dto.*;
import findo.booking.entity.Booking;
import findo.booking.service.impl.BookingServiceImpl;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<Page<BookingResponseDTO>> getAllBookingHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "updatedTime") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {

        Sort sort = Sort.by(sortDir.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<BookingResponseDTO> bookingHistory = bookingService.getAllBookings(pageable);
        return ResponseEntity.ok(bookingHistory);
    }

    // Show Booking History (Customer)
    @GetMapping("/customer/booking-history")
    public Mono<ResponseEntity<Page<CustomerBookingHistoryDTO>>> getBookingHistory(
            @AuthenticationPrincipal JwtAuthenticationToken principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "updatedTime") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {

        String userId = principal.getToken().getClaimAsString("sub");

        Sort sort = Sort.by(sortDir.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return bookingService.getBookingHistoryByUser(UUID.fromString(userId), pageable)
                .map(ResponseEntity::ok);
    }

    // Show Booking Detail (Customer)
    @GetMapping("/customer/history/detail/{bookingId}")
    public BookingDetailDTO getBookingDetail(@PathVariable UUID bookingId) {
        return bookingService.getBookingDetail(bookingId);
    }

    @PostMapping("/customer/book")
    public Mono<Booking> createBooking(
            @AuthenticationPrincipal JwtAuthenticationToken principal,
            @RequestBody CreateBookingDTO request) {

        // Extract user ID from JWT token's "sub" claim
        String userId = principal.getToken().getClaimAsString("sub");
        String token = principal.getToken().getTokenValue();
        String email = principal.getToken().getClaimAsString("email");

        return bookingService.createBooking(request, UUID.fromString(userId), token, email);
    }

    @PatchMapping("/customer/print-ticket/{bookingId}")
    public Mono<ResponseEntity<InputStreamResource>> printTicket(
            @PathVariable UUID bookingId) {
        return bookingService.printTicket(bookingId)
                .map(responseDTO -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Content-Disposition", "inline; filename=booking.pdf");

                    return ResponseEntity.ok()
                            .headers(headers)
                            .contentType(MediaType.APPLICATION_PDF)
                            .body(new InputStreamResource(responseDTO.getPdfInputStream()));
                });
    }
}