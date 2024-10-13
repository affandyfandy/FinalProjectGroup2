package findo.schedule.repository;

import findo.schedule.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
    @Query("SELECT s FROM Schedule s WHERE s.showDate BETWEEN :startOfDay AND :endOfDay")
    Page<Schedule> findByShowDateBetween(Timestamp startOfDay, Timestamp endOfDay, Pageable pageable);

    @Query("SELECT s FROM Schedule s JOIN s.movieId m WHERE m IN :movieIds")
    Page<Schedule> findByMovieId(@Param("movieIds") List<UUID> movieIds, Pageable pageable);

    @Query("SELECT s FROM Schedule s JOIN s.movieId m WHERE m IN :movieIds AND s.showDate BETWEEN :startOfDay AND :endOfDay")
    Page<Schedule> findByMovieIdAndShowDateRange(@Param("movieIds") List<UUID> movieIds,
            @Param("startOfDay") Timestamp startOfDay,
            @Param("endOfDay") Timestamp endOfDay,
            Pageable pageable);

}
