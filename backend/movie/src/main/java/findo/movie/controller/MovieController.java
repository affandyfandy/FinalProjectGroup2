package findo.movie.controller;

import java.util.HashMap;
import java.util.Map;
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

    @GetMapping(value = "/admin/get-all")
    public ResponseEntity<Page<Movie>> getAllMovies(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size, @RequestParam(defaultValue = "default") String searchKey) {
        Pageable pageable = PageRequest.of(page, size);

        if (!searchKey.equals("default")) {
            Page<Movie> movieSearchPage = movieService.findAllMoviesByTitle(searchKey, pageable);

            if (movieSearchPage.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
    
            return ResponseEntity.status(HttpStatus.OK).body(movieSearchPage);
        }

        Page<Movie> moviePage = movieService.findAllMovies(pageable);

        if (moviePage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(moviePage);
    }

    @GetMapping(value = "/anonymous/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable("id") UUID id) {
        Movie movie = movieService.findMovieById(id);

        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    @PostMapping(value = "/admin/create-movie")
    public ResponseEntity<Movie> createMovie(@Valid @RequestBody MovieSaveDTO movieSaveDTO) {
        Movie movie = movieService.createMovie(movieSaveDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(movie);
    }

    @PutMapping(value = "/admin/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable("id") UUID id,
            @Valid @RequestBody MovieSaveDTO movieSaveDTO) {
        Movie movie = movieService.updateMovie(id, movieSaveDTO);

        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }

    @PostMapping(value = "/admin/upload-poster")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        String url = movieService.uploadFile(file);

        Map<String, String> urlResponse = new HashMap<>();
        urlResponse.put("posterUrl", url);

        return ResponseEntity.status(HttpStatus.OK).body(urlResponse);
    }
}
