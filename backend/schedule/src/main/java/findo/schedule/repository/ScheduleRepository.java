package findo.schedule.repository;

import findo.schedule.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
    @Query("SELECT s FROM Schedule s WHERE s.showDate BETWEEN :startOfDay AND :endOfDay")
    Page<Schedule> findByShowDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay, Pageable pageable);
    List<Schedule> findByMovieIdAndShowDate(List<UUID> movieId, Timestamp showDate, Pageable pageable);
}
