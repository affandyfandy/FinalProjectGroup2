package findo.schedule.service;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Page;

import findo.schedule.dto.AvailableScheduleDTO;
import findo.schedule.dto.CreateScheduleDTO;
import findo.schedule.dto.ScheduleDetailDTO;
import findo.schedule.dto.ScheduleResponseDTO;
import reactor.core.publisher.Mono;

public interface ScheduleService {
    ScheduleResponseDTO createSchedule(CreateScheduleDTO dto, String email);

    Mono<Page<ScheduleDetailDTO>> getSchedulesByMovieId(UUID movieId, int page, int size, LocalDate showDate,
            String token);

    Mono<Page<AvailableScheduleDTO>> getAvailableSchedules(LocalDate showDate, int page, int size,
            String token);
}
