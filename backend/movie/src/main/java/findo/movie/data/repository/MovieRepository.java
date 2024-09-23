package findo.movie.data.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import findo.movie.data.entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {
    boolean existsByTitle(String title);
}
