package findo.schedule.controller;

import findo.schedule.dto.CreateScheduleDTO;
import findo.schedule.dto.MovieScheduleDTO;
import findo.schedule.dto.MovieShowtimeDTO;
import findo.schedule.dto.ScheduleResponseDTO;
import findo.schedule.service.impl.ScheduleServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleServiceImpl scheduleService;

    @PostMapping("/admin/create-schedule")
    public ResponseEntity<ScheduleResponseDTO> createSchedule(@Valid @RequestBody CreateScheduleDTO dto,
            @AuthenticationPrincipal JwtAuthenticationToken principal) {
        String email = principal.getToken().getClaimAsString("email");
        ScheduleResponseDTO schedule = scheduleService.createSchedule(dto, email);
        return new ResponseEntity<>(schedule, HttpStatus.CREATED);
    }

    // Endpoint to get available schedules filtered by date and paginated
    @GetMapping("/available")
    public Mono<ResponseEntity<Page<MovieScheduleDTO>>> getAvailableSchedules(
            @AuthenticationPrincipal JwtAuthenticationToken principal,
            @RequestParam("showDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate showDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        String userId = principal.getToken().getClaimAsString("sub");
        Pageable pageable = PageRequest.of(page, size); // Create a Pageable object using the parameters

        return scheduleService.getAvailableSchedules(showDate, pageable, userId)
                .map(schedulePage -> ResponseEntity.ok(schedulePage))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Endpoint to get showtimes for a specific movie, filtered by date and
    // paginated
    @GetMapping("/{movieId}/showtimes")
    public Mono<ResponseEntity<Page<MovieShowtimeDTO>>> getMovieShowtimes(
            @AuthenticationPrincipal JwtAuthenticationToken principal,
            @PathVariable("movieId") UUID movieId,
            @RequestParam("showDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate showDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        String userId = principal.getToken().getClaimAsString("sub");
        Pageable pageable = PageRequest.of(page, size); // Create a Pageable object using the parameters

        return scheduleService.getMovieShowtime(movieId, showDate, pageable, userId)
                .map(showtimePage -> ResponseEntity.ok(showtimePage))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}