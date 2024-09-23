package findo.schedule.repository;

import findo.schedule.entity.Schedule;
import reactor.core.publisher.Flux;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.UUID;

public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
    @Query("SELECT DISTINCT s.movieId FROM Schedule s WHERE s.showDate = :showDate")
    Flux<UUID> findDistinctByShowDate(@Param("showDate") LocalDate showDate, Pageable pageable);

    Flux<Schedule> findByMovieIdAndShowDate(UUID movieId, LocalDate showDate, Pageable pageable);
}
