package findo.schedule.service.impl;

import findo.schedule.client.MovieClient;
import findo.schedule.client.StudioClient;
import findo.schedule.dto.*;
import findo.schedule.entity.Schedule;
import findo.schedule.mapper.ScheduleMapper;
import findo.schedule.repository.ScheduleRepository;
import findo.schedule.service.ScheduleService;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final MovieClient movieClient;
    private final ScheduleMapper scheduleMapper;
    private final StudioClient studioClient;

    ScheduleServiceImpl(ScheduleRepository scheduleRepository, MovieClient movieClient, ScheduleMapper scheduleMapper,
            StudioClient studioClient) {
        this.scheduleRepository = scheduleRepository;
        this.movieClient = movieClient;
        this.scheduleMapper = scheduleMapper;
        this.studioClient = studioClient;
    }

    @Override
    public ScheduleResponseDTO createSchedule(CreateScheduleDTO dto, String email) {
        Schedule schedule = scheduleMapper.toEntity(dto);
        schedule.setCreatedTime(Timestamp.from(Instant.now()));
        schedule.setUpdatedTime(Timestamp.from(Instant.now()));
        schedule.setCreatedBy(email);
        schedule.setUpdatedBy(email);
        try {
            Schedule savedSchedule = scheduleRepository.save(schedule);
            return scheduleMapper.toResponseDto(savedSchedule);
        } catch (Exception e) {
            throw new RuntimeException("Error while creating schedule", e);
        }
    }

    public Mono<Page<AvailableScheduleDTO>> getAvailableSchedules(LocalDate showDate, int page, int size, String token) {
        Pageable pageable = PageRequest.of(page, size);

        // Convert LocalDate to start and end of day
        LocalDateTime startOfDay = showDate.atStartOfDay();
        LocalDateTime endOfDay = showDate.atTime(LocalTime.MAX);

        Page<Schedule> schedules = scheduleRepository.findByShowDateBetween(startOfDay, endOfDay, pageable);

        List<Mono<AvailableScheduleDTO>> dtos = schedules.getContent().stream()
                .map(schedule -> {
                    Mono<MovieDTO> movie = movieClient.getMovieById(schedule.getMovieId(), token);
                    Mono<StudioDTO> studio = studioClient.getStudioById(schedule.getStudioId(), token);

                    return Mono.zip(movie, studio, (m, s) ->
                            new AvailableScheduleDTO(m.getId(), m.getTitle(),
                                    m.getSynopsis(), m.getYear(),
                                    m.getDuration(), s.getName()));
                })
                .collect(Collectors.toList());

        return Mono.zip(dtos, array -> {
            List<AvailableScheduleDTO> availableSchedules = Arrays.stream(array)
                    .map(o -> (AvailableScheduleDTO) o)
                    .collect(Collectors.toList());

            return new PageImpl<>(availableSchedules, pageable, schedules.getTotalElements());
        });
    }

    public Mono<List<ShowtimeDTO>> getShowtimes(List<UUID> movieId, Timestamp date, Pageable pageable, String token) {
        // Get the schedules from the repository
        return Mono.fromCallable(() -> scheduleRepository.findByMovieIdAndShowDate(movieId, date, pageable))
                .flatMap(schedules -> {
                    // Create a list of Mono<ShowtimeDTO> from the schedules
                    List<Mono<ShowtimeDTO>> dtos = schedules.stream()
                            .map(schedule -> studioClient.getStudioById(schedule.getStudioId(), token)
                                    .map(studioDTO -> new ShowtimeDTO(schedule.getShowDate(), studioDTO.getName())))
                            .collect(Collectors.toList());

                    // Zip all the Monos into one and return as a List<ShowtimeDTO>
                    return Mono.zip(dtos, array -> Arrays.stream(array)
                            .map(o -> (ShowtimeDTO) o)
                            .collect(Collectors.toList()));
                });
    }

    public Page<Schedule> findAllSchedule(Pageable pageable) {
        return scheduleRepository.findAll(pageable);
    }

}
