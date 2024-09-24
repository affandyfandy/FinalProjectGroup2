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

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleServiceImpl scheduleService;

    @GetMapping("/admin")
    public Mono<ResponseEntity<Page<ScheduleDTO>>> getAllSchedules(
            @AuthenticationPrincipal JwtAuthenticationToken principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        String token = principal.getToken().getTokenValue();

        Pageable pageable = PageRequest.of(page, size);

        // Fetch paginated schedule details with movie and studio data
        return scheduleService.findAllSchedule(pageable, token)
                .map(schedulePage -> {
                    if (schedulePage.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                    } else {
                        return ResponseEntity.status(HttpStatus.OK).body(schedulePage);
                    }
                });
    }

    @PostMapping("/admin/create-schedule")
    public ResponseEntity<ScheduleResponseDTO> createSchedule(@Valid @RequestBody CreateScheduleDTO dto,
            @AuthenticationPrincipal JwtAuthenticationToken principal) {
        String email = principal.getToken().getClaimAsString("email");
        ScheduleResponseDTO schedule = scheduleService.createSchedule(dto, email);
        return new ResponseEntity<>(schedule, HttpStatus.CREATED);
    }

    @GetMapping("/{scheduleId}")
    public Mono<ResponseEntity<ScheduleDetailDTO>> getScheduleById(@PathVariable UUID scheduleId,
            @AuthenticationPrincipal JwtAuthenticationToken principal) {
        String token = principal.getToken().getTokenValue();

        return scheduleService.findScheduleDetailById(scheduleId, token)
                .map(scheduleDetailDTO -> ResponseEntity.status(HttpStatus.OK).body(scheduleDetailDTO))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/available")
    public Mono<ResponseEntity<Page<AvailableScheduleDTO>>> getAvailableSchedules(
            @RequestParam @Valid LocalDate showDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal JwtAuthenticationToken principal) {
        String token = principal.getToken().getTokenValue();
        return scheduleService.getAvailableSchedules(showDate, page, size, token)
                .map(availableSchedulesPage -> {
                    if (availableSchedulesPage.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                    }
                    return ResponseEntity.status(HttpStatus.OK).body(availableSchedulesPage);
                });
    }

    @GetMapping("/movie/{movieId}")
    public Mono<ResponseEntity<Page<ScheduleDetailDTO>>> getSchedulesByMovieId(
            @PathVariable UUID movieId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) @Valid LocalDate showDate,
            @AuthenticationPrincipal JwtAuthenticationToken principal) {

        // Get the JWT token from the authenticated user
        String token = principal.getToken().getTokenValue();

        // Call the service method and return the response
        return scheduleService.getSchedulesByMovieId(movieId, page, size, showDate, token)
                .map(schedulePage -> {
                    if (schedulePage.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // Return 204 if no content
                    }
                    return ResponseEntity.ok(schedulePage); // Return 200 with the page of schedule details
                });
    }
}