package findo.movie.service.impl;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
            .orElseThrow(() -> new MovieNotFoundException("Movie not found!"));
    }

    @Override
    public Movie createMovie(MovieSaveDTO movieSaveDTO) {
        if(movieRepository.existsByTitle(movieSaveDTO.getTitle())) {
            throw new DuplicateTitleException("Movie with this title already exists!");
        }

        Movie savedMovie = movieMapper.toMovie(movieSaveDTO);

        savedMovie.setDuration(15);
        savedMovie.setCreatedBy("test");
        savedMovie.setUpdatedBy("test");
        savedMovie.setCreatedTime(LocalDate.now());
        savedMovie.setUpdatedTime(LocalDate.now());

        return movieRepository.save(savedMovie);
    }

    @Override
    public Movie updateMovie(UUID id, MovieSaveDTO movieSaveDTO) {
        Movie checkMovie = findMovieById(id);

        if(!checkMovie.getTitle().equals(movieSaveDTO.getTitle()) && movieRepository.existsByTitle(movieSaveDTO.getTitle())) {
            throw new DuplicateTitleException("Movie with title already exists!");
        }

        checkMovie.setTitle(movieSaveDTO.getTitle());
        checkMovie.setSynopsis(movieSaveDTO.getSynopsis());
        checkMovie.setPosterUrl(movieSaveDTO.getPosterUrl());
        checkMovie.setYear(movieSaveDTO.getYear());
        checkMovie.setUpdatedBy("test");
        checkMovie.setUpdatedTime(LocalDate.now());

        return movieRepository.save(checkMovie);
    }

    @Override
    public String uploadFile(MultipartFile file) {
        return imgUtils.uploadFile(file);
    }
}
