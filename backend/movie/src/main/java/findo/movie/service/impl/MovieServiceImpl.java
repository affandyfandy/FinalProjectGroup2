package findo.movie.service.impl;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import findo.movie.data.entity.Movie;
import findo.movie.data.repository.MovieRepository;
import findo.movie.dto.MovieSaveDTO;
import findo.movie.mapper.MovieMapper;
import findo.movie.service.MovieService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    @Override
    public Page<Movie> findAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    @Override
    public Movie findMovieById(UUID id) {
        return movieRepository.findById(id).orElse(null);
    }

    @Override
    public Movie createMovie(MovieSaveDTO movieSaveDTO) {
        Movie movie = movieMapper.toMovie(movieSaveDTO);
        movie.setCreatedBy("test");
        movie.setUpdatedBy("test");
        movie.setCreatedTime(LocalDate.now());
        movie.setUpdatedTime(LocalDate.now());

        return movieRepository.save(movie);
    }

    @Override
    public Movie updateMovie(UUID id, MovieSaveDTO movieSaveDTO) {
        Movie checkMovie = findMovieById(id);

        if(checkMovie != null) {
            checkMovie.setTitle(movieSaveDTO.getTitle());
            checkMovie.setSynopsis(movieSaveDTO.getSynopsis());
            checkMovie.setPosterUrl(movieSaveDTO.getPosterUrl());
            checkMovie.setYear(movieSaveDTO.getYear());
            checkMovie.setDuration(movieSaveDTO.getDuration());
            checkMovie.setUpdatedBy("test");
            checkMovie.setUpdatedTime(LocalDate.now());

            return movieRepository.save(checkMovie);
        }

        return null;
    }
}
