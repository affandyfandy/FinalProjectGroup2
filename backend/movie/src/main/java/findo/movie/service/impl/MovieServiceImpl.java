package findo.movie.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import findo.movie.core.AppConstant;
import findo.movie.data.entity.Movie;
import findo.movie.data.repository.MovieRepository;
import findo.movie.dto.MovieSaveDTO;
import findo.movie.exception.DuplicateTitleException;
import findo.movie.exception.MovieNotFoundException;
import findo.movie.mapper.MovieMapper;
import findo.movie.service.MovieService;
import findo.movie.utils.ImgUtils;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final ImgUtils imgUtils;

    @Override
    public Page<Movie> findAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    @Override
    public Page<Movie> findAllMoviesByTitle(String title, Pageable pageable) {
        return movieRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    @Override
    public Movie findMovieById(UUID id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(AppConstant.MovieNotFoundMsg.getValue()));
    }

    @Override
    public Movie createMovie(MovieSaveDTO movieSaveDTO, String email) {
        if (movieRepository.existsByTitle(movieSaveDTO.getTitle())) {
            throw new DuplicateTitleException(AppConstant.MovieAlreadyExistMsg.getValue());
        }

        Movie savedMovie = movieMapper.toMovie(movieSaveDTO);

        savedMovie.setDuration(15);
        savedMovie.setCreatedBy(email);
        savedMovie.setUpdatedBy(email);
        savedMovie.setCreatedTime(Timestamp.from(Instant.now()));
        savedMovie.setUpdatedTime(Timestamp.from(Instant.now()));

        return movieRepository.save(savedMovie);
    }

    @Override
    public Movie updateMovie(UUID id, MovieSaveDTO movieSaveDTO, String email) {
        Movie checkMovie = findMovieById(id);

        if (!checkMovie.getTitle().equals(movieSaveDTO.getTitle())
                && movieRepository.existsByTitle(movieSaveDTO.getTitle())) {
            throw new DuplicateTitleException(AppConstant.MovieAlreadyExistMsg.getValue());
        }

        checkMovie.setTitle(movieSaveDTO.getTitle());
        checkMovie.setSynopsis(movieSaveDTO.getSynopsis());
        checkMovie.setPosterUrl(movieSaveDTO.getPosterUrl());
        checkMovie.setYear(movieSaveDTO.getYear());
        checkMovie.setUpdatedBy(email);
        checkMovie.setUpdatedTime(Timestamp.from(Instant.now()));

        return movieRepository.save(checkMovie);
    }

    @Override
    public String uploadFile(MultipartFile file) {
        return imgUtils.uploadFile(file);
    }
}
