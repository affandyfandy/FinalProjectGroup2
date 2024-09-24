package findo.schedule.service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import findo.schedule.dto.AvailableScheduleDTO;
import findo.schedule.dto.CreateScheduleDTO;
import findo.schedule.dto.ScheduleDTO;
import findo.schedule.dto.ScheduleDetailDTO;
import findo.schedule.dto.ScheduleResponseDTO;
import reactor.core.publisher.Mono;

public interface ScheduleService {
        ScheduleResponseDTO createSchedule(CreateScheduleDTO dto, String email);

        Mono<Page<ScheduleDetailDTO>> getSchedulesByMovieId(UUID movieId, int page, int size, LocalDate showDate,
                        String token);

        Mono<Page<AvailableScheduleDTO>> getAvailableSchedules(LocalDate showDate, int page, int size,
                        String token);

        Mono<ScheduleDetailDTO> findScheduleDetailById(UUID scheduleId, String token);

        Mono<Page<ScheduleDTO>> findAllSchedule(Pageable pageable, LocalDate date, String token);

}
