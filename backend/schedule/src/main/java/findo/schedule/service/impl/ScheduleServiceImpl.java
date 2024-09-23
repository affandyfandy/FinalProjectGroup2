package findo.schedule.service.impl;

import findo.schedule.client.MovieClient;
import findo.schedule.client.StudioClient;
import findo.schedule.dto.CreateScheduleDTO;
import findo.schedule.dto.MovieDTO;
import findo.schedule.dto.MovieScheduleDTO;
import findo.schedule.dto.MovieShowtimeDTO;
import findo.schedule.dto.ScheduleResponseDTO;
import findo.schedule.dto.ShowtimeDTO;
import findo.schedule.dto.StudioDTO;
import findo.schedule.entity.Schedule;
import findo.schedule.mapper.ScheduleMapper;
import findo.schedule.repository.ScheduleRepository;
import findo.schedule.service.ScheduleService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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

    // Get paginated list of available schedules, filtered by showDate
    @Override
    public Mono<Page<MovieScheduleDTO>> getAvailableSchedules(LocalDate showDate, Pageable pageable, String token) {
        return scheduleRepository.findDistinctByShowDate(showDate, pageable)
                .flatMap(movieId -> scheduleRepository.findByMovieIdAndShowDate(movieId, showDate, pageable)
                        .flatMap(schedule -> {
                            // Use the updated method to get movie by single ID
                            Mono<MovieDTO> movieMono = movieClient.getMovieById(movieId, token);
                            return movieMono.map(movie -> new MovieScheduleDTO(
                                    movie.getTitle(),
                                    movie.getSynopsis(),
                                    movie.getYear(),
                                    schedule.getPrice()));
                        }))
                .collectList()
                .flatMap(movieScheduleDTOs -> {
                    Page<MovieScheduleDTO> page = new PageImpl<>(movieScheduleDTOs, pageable, movieScheduleDTOs.size());
                    return Mono.just(page);
                });
    }

    @Override
    public Mono<Page<MovieShowtimeDTO>> getMovieShowtime(UUID movieId, LocalDate showDate, Pageable pageable,
            String token) {
        return scheduleRepository.findByMovieIdAndShowDate(movieId, showDate, pageable)
                .collectList() // Collect all schedules into a list first
                .flatMap(schedules -> {
                    // If no schedules found, return an empty page
                    if (schedules.isEmpty()) {
                        return Mono.just(new PageImpl<>(List.of(), pageable, 0));
                    }

                    // Extract the studio IDs and movie details
                    List<Mono<StudioDTO>> studioMonos = schedules.stream()
                            .map(schedule -> studioClient.getStudioById(schedule.getStudioId(), token))
                            .toList();

                    // Fetch movie details
                    Mono<MovieDTO> movieMono = movieClient.getMovieById(movieId, token);

                    return movieMono.flatMap(movie -> Flux.merge(studioMonos) // Combine all studio fetch operations
                            .collectList() // Collect all studio data
                            .map(studios -> {
                                // Create showtimes from the collected data
                                List<ShowtimeDTO> showtimes = schedules.stream()
                                        .map(schedule -> {
                                            StudioDTO studio = studios.get(schedules.indexOf(schedule)); // Get the
                                                                                                         // corresponding
                                                                                                         // studio
                                            return new ShowtimeDTO(
                                                    schedule.getShowDate(),
                                                    studio.getName(),
                                                    schedule.getPrice());
                                        })
                                        .toList();

                                // Create the final MovieShowtimeDTO
                                MovieShowtimeDTO movieShowtimeDTO = new MovieShowtimeDTO(
                                        movie.getTitle(),
                                        movie.getSynopsis(),
                                        movie.getYear(),
                                        movie.getDuration(),
                                        showtimes);

                                // Wrap in a Page
                                return new PageImpl<>(List.of(movieShowtimeDTO), pageable, 1);
                            }));
                });
    }

}
