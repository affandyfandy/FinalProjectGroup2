package findo.movie.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import findo.movie.data.entity.Movie;
import findo.movie.dto.MovieSaveDTO;
import findo.movie.service.MovieService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/movies")
@Validated
@AllArgsConstructor
public class MovieController {
    
    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<Page<Movie>> getAllCustomer(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Movie> moviePage = movieService.findAllMovies(pageable);

        if(moviePage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(moviePage);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Movie> getCustomerById(@PathVariable("id") UUID id) {
        Movie movie = movieService.findMovieById(id);

        if(movie == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    @PostMapping
    public ResponseEntity<Movie> createMovie(@Valid @RequestBody MovieSaveDTO movieSaveDTO) {
        Movie movie = movieService.createMovie(movieSaveDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(movie);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable("id") UUID id, @Valid @RequestBody MovieSaveDTO movieSaveDTO) {
        Movie movie = movieService.updateMovie(id, movieSaveDTO);

        if(movie == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    @PostMapping(value = "/upload-poster")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String url = movieService.uploadFile(file);
        return ResponseEntity.status(HttpStatus.OK).body(url);
    }
}
