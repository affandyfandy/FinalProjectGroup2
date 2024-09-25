
package findo.booking.controller;

import findo.booking.client.ScheduleClient;
import findo.booking.client.StudioClient;
import findo.booking.client.UserClient;
import findo.booking.dto.*;
import findo.booking.entity.Booking;
import findo.booking.entity.BookingSeat;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingServiceImpl bookingService;
    private final StudioClient studioClient;
    private final ScheduleClient scheduleClient;
    private final UserClient userClient;

    public BookingController(BookingServiceImpl bookingService, StudioClient studioClient,
            ScheduleClient scheduleClient, UserClient userClient) {
        this.bookingService = bookingService;
        this.studioClient = studioClient;
        this.scheduleClient = scheduleClient;
        this.userClient = userClient;
    }

    // Show All Booking History (Admin)
    @GetMapping("/admin/history")
    public Mono<ResponseEntity<Page<BookingResponseDTO>>> getAllBookingHistory(
            @AuthenticationPrincipal JwtAuthenticationToken principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "updatedTime") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        String token = principal.getToken().getTokenValue();
        Sort sort = Sort.by(sortDir.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return bookingService.getAllBookings(pageable, token)
                .map(bookingHistory -> ResponseEntity.ok(bookingHistory))
                .defaultIfEmpty(ResponseEntity.notFound().build()); // Handle case if there are no bookings
    }

    // Show Booking History (Customer)
    @GetMapping("/customer/booking-history")
    public Mono<ResponseEntity<Page<BookingResponseDTO>>> getBookingHistory(
            @AuthenticationPrincipal JwtAuthenticationToken principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "updatedTime") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {

        String userId = principal.getToken().getClaimAsString("sub");

        Sort sort = Sort.by(sortDir.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return bookingService
                .getBookingHistoryByUser(UUID.fromString(userId), pageable, principal.getToken().getTokenValue())
                .map(ResponseEntity::ok);
    }

    // Show Booking Detail (Customer)
    @GetMapping("/customer/history/detail/{bookingId}")
    public BookingDetailDTO getBookingDetail(@AuthenticationPrincipal JwtAuthenticationToken principal,
            @PathVariable UUID bookingId) {
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
            @PathVariable UUID bookingId, @AuthenticationPrincipal JwtAuthenticationToken principal) {
        String email = principal.getToken().getClaimAsString("email");
        return bookingService.printTicket(bookingId, email)
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

    @GetMapping("/test_connection/{bookingId}")
    public ResponseEntity<?> testAja(@PathVariable("bookingId") UUID bookingId,
            @AuthenticationPrincipal JwtAuthenticationToken principal) {

        String token = principal.getToken().getTokenValue();

        Booking booking = bookingService.getBookingById(bookingId);

        UUID custId = booking.getUserIds();

        Mono<UserDTO> custMono = userClient.getUserById(custId, token);

        List<Integer> seatIds = booking.getBookingSeats().get(0).getSeatIds(); // list Seat ID
        Mono<SeatDTO> seatCode = studioClient.getSeatById(seatIds.get(0), token); // convert Seat ID to SeatCode

        UUID scheduleIds = booking.getScheduleIds().get(0);
        Mono<ScheduleMovieClientDTO> listScheduleMono = scheduleClient.getScheduleByIds(scheduleIds, token);

        Mono<ScheduleDetailsAdmin> result = Mono.zip(custMono, listScheduleMono)
                .flatMap(tuple -> {
                    UserDTO cust = tuple.getT1();
                    ScheduleMovieClientDTO listSchedule = tuple.getT2();

                    ScheduleDetailsAdmin test = new ScheduleDetailsAdmin();

                    test.setBookingId(bookingId);
                    test.setCustId(custId);
                    test.setCustName(cust.getName());
                    test.setMovies(null);
                    test.setSchedules(null);
                    test.setSeats(null);
                    test.setStudios(null);
                    test.setTotalAmount(0);

                    return Mono.just(test);
                });

        return ResponseEntity.ok().body(result);
    }
}
