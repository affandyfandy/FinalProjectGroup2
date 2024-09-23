package findo.schedule.service;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import findo.schedule.dto.CreateScheduleDTO;
import findo.schedule.dto.MovieScheduleDTO;
import findo.schedule.dto.MovieShowtimeDTO;
import findo.schedule.dto.ScheduleResponseDTO;
import reactor.core.publisher.Mono;

public interface ScheduleService {
    ScheduleResponseDTO createSchedule(CreateScheduleDTO dto, String email);

    Mono<Page<MovieShowtimeDTO>> getMovieShowtime(UUID movieId, LocalDate showDate, Pageable pageable, String token);

    Mono<Page<MovieScheduleDTO>> getAvailableSchedules(LocalDate showDate, Pageable pageable, String token);
}
