
package findo.booking.controller;

import findo.booking.dto.*;
import findo.booking.entity.Booking;
import findo.booking.service.impl.BookingServiceImpl;
import jakarta.validation.Valid;
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

import java.time.LocalDate;
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
    public Mono<ResponseEntity<Page<BookingResponseDTO>>> getAllBookingHistory(
            @AuthenticationPrincipal JwtAuthenticationToken principal,
            @RequestParam @Valid LocalDate date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "updatedTime") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        String token = principal.getToken().getTokenValue();
        Sort sort = Sort.by(sortDir.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return bookingService.getAllBookings(pageable, date, token)
                .map(bookingHistory -> ResponseEntity.ok(bookingHistory))
                .defaultIfEmpty(ResponseEntity.notFound().build()); // Handle case if there are no bookings
    }

    // Show Booking History (Customer)
    @GetMapping("/customer/history")
    public Mono<ResponseEntity<Page<BookingResponseDTO>>> getBookingHistory(
            @AuthenticationPrincipal JwtAuthenticationToken principal,
            @RequestParam @Valid LocalDate date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "updatedTime") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {

        String userId = principal.getToken().getClaimAsString("sub");

        Sort sort = Sort.by(sortDir.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return bookingService
                .getBookingHistoryByUser(UUID.fromString(userId), pageable, date, principal.getToken().getTokenValue())
                .map(ResponseEntity::ok);
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
            @PathVariable UUID bookingId, @AuthenticationPrincipal JwtAuthenticationToken principal) {
        String email = principal.getToken().getClaimAsString("email");
        String token = principal.getToken().getTokenValue();
        return bookingService.printTicket(bookingId, email, token)
                .map(responseDTO -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Content-Disposition", "inline; filename=booking.pdf");

                    return ResponseEntity.ok()
                            .headers(headers)
                            .contentType(MediaType.APPLICATION_PDF)
                            .body(new InputStreamResource(responseDTO.getPdfInputStream()));
                });
    }

    @GetMapping("/{scheduleId}/seats-ids")
    public ResponseEntity<BookingSeatsDTO> getSeatIds(@PathVariable("scheduleId") UUID scheduleId) {
        BookingSeatsDTO seatIds = bookingService.getAllSeatIds(scheduleId);

        return ResponseEntity.ok().body(seatIds);
    }

    @GetMapping("/history/detail/{bookingId}")
    public ResponseEntity<Mono<ScheduleDetailsAdmin>> getBookingHistoryDetail(@PathVariable("bookingId") UUID bookingId,
            @AuthenticationPrincipal JwtAuthenticationToken principal) {

        String token = principal.getToken().getTokenValue();

        Mono<ScheduleDetailsAdmin> scheduleDetails = bookingService.getBookingDetails(bookingId, token);

        return ResponseEntity.ok().body(scheduleDetails);
    }
}
