package findo.movie.mapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import org.joda.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import findo.movie.data.entity.Movie;
import findo.movie.dto.MovieDTO;
import findo.movie.dto.MovieSaveDTO;

class MovieMapperTest {

    private MovieMapper movieMapper;

    private Movie movie;
    private MovieDTO movieDTO;
    private MovieSaveDTO movieSaveDTO;

    @BeforeEach
    void init() {
        movieMapper = Mappers.getMapper(MovieMapper.class);

        movie = new Movie(UUID.randomUUID(), "Marvel", "This is all about Super Heroes", 30, "http://list", 2024,
                Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), "Admin", "Admin");
        movieDTO = new MovieDTO(UUID.randomUUID(), "Marvel", "This is all about Super Heroes", 30, "http://list", 2024);
        movieSaveDTO = new MovieSaveDTO("Marvel", "This is all about Super Heroes", "http://list", 2024);
    }

    @Test
    void movieMapper_toMovieDTO_returnMovieDTO() {
        MovieDTO movDTO = movieMapper.toMovieDTO(movie);

        Assertions.assertEquals(movie.getId(), movDTO.getId());
        Assertions.assertEquals(movie.getTitle(), movDTO.getTitle());
        Assertions.assertEquals(movie.getSynopsis(), movDTO.getSynopsis());
        Assertions.assertEquals(movie.getDuration(), movDTO.getDuration());
        Assertions.assertEquals(movie.getPosterUrl(), movDTO.getPosterUrl());
        Assertions.assertEquals(movie.getYear(), movDTO.getYear());
    }

    @Test
    void movieMapper_toMovieFromMovieDTO_returnMovie() {
        Movie mov = movieMapper.toMovie(movieDTO);

        Assertions.assertEquals(movieDTO.getId(), mov.getId());
        Assertions.assertEquals(movieDTO.getTitle(), mov.getTitle());
        Assertions.assertEquals(movieDTO.getSynopsis(), mov.getSynopsis());
        Assertions.assertEquals(movieDTO.getDuration(), mov.getDuration());
        Assertions.assertEquals(movieDTO.getPosterUrl(), mov.getPosterUrl());
        Assertions.assertEquals(movieDTO.getYear(), mov.getYear());
        Assertions.assertNull(mov.getCreatedTime());
        Assertions.assertNull(mov.getUpdatedTime());
        Assertions.assertNull(mov.getCreatedBy());
        Assertions.assertNull(mov.getUpdatedBy());
    }

    @Test
    void movieMapper_toMovieSaveDTO_returnMovieSaveDTO() {
        MovieSaveDTO movSaveDTO = movieMapper.toMovieSaveDTO(movie);

        Assertions.assertEquals(movie.getTitle(), movSaveDTO.getTitle());
        Assertions.assertEquals(movie.getSynopsis(), movSaveDTO.getSynopsis());
        Assertions.assertEquals(movie.getPosterUrl(), movSaveDTO.getPosterUrl());
        Assertions.assertEquals(movie.getYear(), movSaveDTO.getYear());
    }

    @Test
    void movieMapper_toMovieFromMovieSaveDTO_returnMovie() {
        Movie mov = movieMapper.toMovie(movieSaveDTO);

        Assertions.assertEquals(movieSaveDTO.getTitle(), mov.getTitle());
        Assertions.assertEquals(movieSaveDTO.getSynopsis(), mov.getSynopsis());
        Assertions.assertEquals(movieSaveDTO.getPosterUrl(), mov.getPosterUrl());
        Assertions.assertEquals(movieSaveDTO.getYear(), mov.getYear());
        Assertions.assertEquals(0, mov.getDuration());
        Assertions.assertEquals(0, mov.getDuration());
        Assertions.assertNull(mov.getId());
        Assertions.assertNull(mov.getCreatedTime());
        Assertions.assertNull(mov.getUpdatedTime());
        Assertions.assertNull(mov.getCreatedBy());
        Assertions.assertNull(mov.getUpdatedBy());
    }
}
