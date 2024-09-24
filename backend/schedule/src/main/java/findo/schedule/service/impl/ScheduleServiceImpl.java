package findo.schedule.service.impl;

import findo.schedule.client.MovieClient;
import findo.schedule.client.StudioClient;
import findo.schedule.dto.*;
import findo.schedule.entity.Schedule;
import findo.schedule.mapper.ScheduleMapper;
import findo.schedule.repository.ScheduleRepository;
import findo.schedule.service.ScheduleService;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Mono;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Override
    public Mono<Page<AvailableScheduleDTO>> getAvailableSchedules(LocalDate showDate, int page, int size,
            String token) {
        Pageable pageable = PageRequest.of(page, size);
        LocalDateTime startOfDay = showDate.atStartOfDay();
        LocalDateTime endOfDay = showDate.atTime(LocalTime.MAX);

        // Fetch schedules using the repository method
        Page<Schedule> schedules = scheduleRepository.findByShowDateBetween(Timestamp.valueOf(startOfDay),
                Timestamp.valueOf(endOfDay), pageable);

        // Group schedules by movieId
        Map<UUID, List<Schedule>> groupedSchedules = schedules.getContent().stream()
                .collect(Collectors.groupingBy(schedule -> schedule.getMovieId().get(0))); // Assuming movieId is a List

        // Prepare the list of DTOs
        List<Mono<AvailableScheduleDTO>> dtos = new ArrayList<>();

        // Aggregate the schedules
        for (Map.Entry<UUID, List<Schedule>> entry : groupedSchedules.entrySet()) {
            UUID movieId = entry.getKey();
            List<Schedule> scheduleList = entry.getValue();

            // Get movie and studio details
            Mono<MovieDTO> movieMono = movieClient.getMovieById(movieId, token);
            Mono<StudioDTO> studioMono = studioClient.getStudioById(scheduleList.get(0).getStudioId().get(0), token); // Assuming
                                                                                                                      // studioId
                                                                                                                      // is
                                                                                                                      // a
                                                                                                                      // list

            dtos.add(Mono.zip(movieMono, studioMono, (movie, studio) -> {
                List<Timestamp> showDates = scheduleList.stream()
                        .map(Schedule::getShowDate) // Collecting show dates, which are Timestamps
                        .collect(Collectors.toList());

                return new AvailableScheduleDTO(movie.getId(), movie.getTitle(),
                        movie.getSynopsis(), movie.getYear(),
                        movie.getDuration(), studio.getName(),
                        movie.getPosterUrl(), showDates);
            }));
        }

        // Zip all results and return in a Mono
        return Mono.zip(dtos, resultsArray -> {
            List<AvailableScheduleDTO> availableSchedules = Arrays.stream(resultsArray)
                    .map(o -> (AvailableScheduleDTO) o)
                    .collect(Collectors.toList());

            return new PageImpl<>(availableSchedules, pageable, schedules.getTotalElements());
        });
    }

    @Override
    public Mono<Page<ScheduleDetailDTO>> getSchedulesByMovieId(UUID movieId, int page, int size, LocalDate showDate,
            String token) {
        Pageable pageable = PageRequest.of(page, size);

        Timestamp startOfDay = null;
        Timestamp endOfDay = null;

        if (showDate != null) {
            startOfDay = Timestamp.valueOf(showDate.atStartOfDay());
            endOfDay = Timestamp.valueOf(showDate.atTime(23, 59, 59)); // End of the day
        }

        // Fetch schedules containing the specified movieId and filtered by date range
        Page<Schedule> schedules;

        if (startOfDay != null && endOfDay != null) {
            schedules = scheduleRepository.findByMovieIdAndShowDateRange(Collections.singletonList(movieId), startOfDay,
                    endOfDay, pageable);
        } else {
            schedules = scheduleRepository.findByMovieId(Collections.singletonList(movieId), pageable);
        }

        if (schedules.isEmpty()) {
            return Mono.just(new PageImpl<>(new ArrayList<>(), pageable, 0));
        }

        // Get movie details using the adjusted repository method
        Mono<MovieDTO> movieMono = movieClient.getMovieById(movieId, token);

        // Prepare the list of DTOs for responses
        Map<Integer, List<ScheduleShowDTO>> studioShowMap = new HashMap<>();

        // Iterate over found schedules
        for (Schedule schedule : schedules) {
            studioShowMap.computeIfAbsent(schedule.getStudioId().get(0), studioId -> new ArrayList<>())
                    .add(new ScheduleShowDTO(schedule.getId(), schedule.getShowDate(), "", schedule.getPrice()));
        }

        return movieMono.flatMap(movie -> {
            List<ScheduleDetailDTO> detailDTOs = new ArrayList<>();

            // Process studio shows
            List<Mono<ScheduleDetailDTO>> detailDTOMonoList = studioShowMap.entrySet().stream()
                    .map(entry -> {
                        Integer studioId = entry.getKey();
                        List<ScheduleShowDTO> shows = entry.getValue();

                        // Get studio info asynchronously
                        return studioClient.getStudioById(studioId, token).map(studio -> {
                            for (ScheduleShowDTO show : shows) {
                                show.setStudioName(studio.getName());
                            }

                            return new ScheduleDetailDTO(
                                    movie.getId(),
                                    movie.getTitle(),
                                    movie.getSynopsis(),
                                    movie.getYear(),
                                    movie.getDuration(),
                                    movie.getPosterUrl(),
                                    shows);
                        });
                    })
                    .collect(Collectors.toList());

            // Combine results into a single PageImpl
            return Mono.zip(detailDTOMonoList, results -> {
                List<ScheduleDetailDTO> finalList = new ArrayList<>();
                for (Object result : results) {
                    finalList.add((ScheduleDetailDTO) result);
                }

                return new PageImpl<>(finalList, pageable, schedules.getTotalElements());
            });
        });
    }

    @Override
    public Page<Schedule> findAllSchedule(Pageable pageable, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        return scheduleRepository.findByShowDateBetween(Timestamp.valueOf(startOfDay),
                Timestamp.valueOf(endOfDay), pageable);
    }

}
