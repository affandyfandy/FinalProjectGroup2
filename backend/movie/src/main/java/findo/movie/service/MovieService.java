package findo.movie.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import findo.movie.data.entity.Movie;
import findo.movie.dto.MovieSaveDTO;

public interface MovieService {
    Page<Movie> findAllMovies(Pageable pageable);
    Movie findMovieById(UUID id);
    Movie createMovie(MovieSaveDTO movieSaveDTO);
    Movie updateMovie(UUID id, MovieSaveDTO movieSaveDTO);
    String uploadFile(MultipartFile file);
}
