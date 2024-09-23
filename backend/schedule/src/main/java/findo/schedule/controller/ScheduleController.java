package findo.schedule.controller;

import findo.schedule.dto.*;
import findo.schedule.entity.Schedule;
import findo.schedule.service.impl.ScheduleServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleServiceImpl scheduleService;


    @GetMapping
    public ResponseEntity<Page<Schedule>> getAllSchedule(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Schedule> schedulePage = scheduleService.findAllSchedule(pageable);

        if(schedulePage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(schedulePage);
    }
    @PostMapping("/admin/create-schedule")
    public ResponseEntity<ScheduleResponseDTO> createSchedule(@Valid @RequestBody CreateScheduleDTO dto,
            @AuthenticationPrincipal JwtAuthenticationToken principal) {
        String email = principal.getToken().getClaimAsString("email");
        ScheduleResponseDTO schedule = scheduleService.createSchedule(dto, email);
        return new ResponseEntity<>(schedule, HttpStatus.CREATED);
    }

    @GetMapping("/available")
    public Mono<ResponseEntity<List<AvailableScheduleDTO>>> getAvailableSchedules(
            @RequestParam @Valid LocalDate showDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal JwtAuthenticationToken principal) {
String token = principal.getToken().getTokenValue();
        return scheduleService.getAvailableSchedules(showDate, page, size, token)
                .map(availableSchedulesPage -> {
                    List<AvailableScheduleDTO> availableSchedules = availableSchedulesPage.getContent();
                    if (availableSchedules.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                    }
                    return ResponseEntity.status(HttpStatus.OK).body(availableSchedules);
                });
    }
    // Endpoint to get showtimes for a specific movie
    @GetMapping("/{movieId}/showtimes")
    public ResponseEntity<List<ShowtimeDTO>> getShowtimes(
            @PathVariable List<UUID> movieId,
            @RequestParam @Valid Timestamp date,
            @AuthenticationPrincipal JwtAuthenticationToken principal,
            Pageable pageable) {

        String token = principal.getToken().getTokenValue();
        List<ShowtimeDTO> showtimes = (List<ShowtimeDTO>) scheduleService.getShowtimes(movieId, date, pageable, token);

        if (showtimes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(showtimes);
    }
}