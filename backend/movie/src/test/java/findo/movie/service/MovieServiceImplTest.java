package findo.movie.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import findo.movie.data.entity.Movie;
import findo.movie.data.repository.MovieRepository;
import findo.movie.dto.MovieSaveDTO;
import findo.movie.exception.DuplicateTitleException;
import findo.movie.exception.MovieNotFoundException;
import findo.movie.mapper.MovieMapper;
import findo.movie.service.impl.MovieServiceImpl;
import findo.movie.utils.ImgUtils;

class MovieServiceImplTest {

    @Mock
    MovieRepository movieRepository;

    @InjectMocks
    MovieServiceImpl movieService;

    @Mock
    MovieMapper movieMapper;

    @Mock
    ImgUtils imgUtils;

    private Movie mov1;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        mov1 = new Movie(UUID.randomUUID(), "Marvel", "This is all about Super Heroes", 30, "http://list", 2024,
                Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), "admi@gmail.com", "admi@gmail.com");
    }

    @Test
    void movieService_findAllMovies_returnMoviePage() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Movie> moviePage = new PageImpl<>(Arrays.asList(mov1));

        when(movieRepository.findAll(pageable)).thenReturn(moviePage);

        Page<Movie> result = movieService.findAllMovies(pageable);

        verify(movieRepository).findAll(pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getTotalElements());
        Assertions.assertEquals(mov1.getTitle(), result.getContent().get(0).getTitle());
    }

    @Test
    void movieService_findMovieById_returnMovie() {
        UUID movId = mov1.getId();

        when(movieRepository.findById(movId)).thenReturn(Optional.of(mov1));

        Movie result = movieService.findMovieById(movId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(movId, result.getId());
    }

    @Test
    void movieService_findMovieById_returnMovieNotFound() {
        UUID movId = UUID.randomUUID();

        when(movieRepository.findById(movId)).thenReturn(Optional.empty());

        Assertions.assertThrows(MovieNotFoundException.class, () -> movieService.findMovieById(movId));
    }

    @Test
    void movieService_createMovie_returnCreatedMovie() {
        MovieSaveDTO movSaveDTO = new MovieSaveDTO("Marvel", "This is all about Super Heroes", "http://list", 2024);

        Movie mov = new Movie();
        mov.setTitle("Marvel");
        mov.setSynopsis("This is all about Super Heroes");
        mov.setPosterUrl("http://list");
        mov.setYear(2024);

        when(movieRepository.existsByTitle(movSaveDTO.getTitle())).thenReturn(false);
        when(movieMapper.toMovie(movSaveDTO)).thenReturn(mov);
        when(movieRepository.save(any(Movie.class))).thenReturn(mov1);

        Movie result = movieService.createMovie(movSaveDTO, "admi@gmail.com");

        Assertions.assertEquals(mov1, result);
    }

    @Test
    void movieService_createMovie_returnDuplicateTitle() {

        MovieSaveDTO movSaveDTO = new MovieSaveDTO("Marvel", "This is all about Super Heroes", "http://list", 2024);

        when(movieRepository.existsByTitle(movSaveDTO.getTitle())).thenReturn(true);

        Assertions.assertThrows(DuplicateTitleException.class,
                () -> movieService.createMovie(movSaveDTO, "admi@gmail.com"));
    }

    @Test
    void movieService_updateMovie_returnUpdatedMovie() {
        UUID movId = mov1.getId();

        MovieSaveDTO movSaveDTO = new MovieSaveDTO("Marvel", "This is all about Super Heroes", "http://list", 2024);

        when(movieRepository.findById(movId)).thenReturn(Optional.of(mov1));
        when(movieRepository.existsByTitle(movSaveDTO.getTitle())).thenReturn(false);
        when(movieRepository.save(any(Movie.class))).thenReturn(mov1);

        Movie result = movieService.updateMovie(movId, movSaveDTO, "admi@gmail.com");

        Assertions.assertEquals(mov1, result);
    }

    @Test
    void movieService_updateMovie_returnMovieNotFound() {
        UUID movId = mov1.getId();

        MovieSaveDTO movSaveDTO = new MovieSaveDTO("Marvel", "This is all about Super Heroes", "http://list", 2024);

        when(movieRepository.findById(movId)).thenReturn(Optional.empty());

        Assertions.assertThrows(MovieNotFoundException.class,
                () -> movieService.updateMovie(movId, movSaveDTO, "admi@gmail.com"));
    }

    @Test
    void movieService_updateMovie_returnDuplicateTitle() {
        UUID movId = mov1.getId();

        MovieSaveDTO movSaveDTO = new MovieSaveDTO("Next Marvel", "This is all about Super Heroes", "http://list",
                2024);

        when(movieRepository.findById(movId)).thenReturn(Optional.of(mov1));
        when(movieRepository.existsByTitle(movSaveDTO.getTitle())).thenReturn(true);

        Assertions.assertThrows(DuplicateTitleException.class,
                () -> movieService.updateMovie(movId, movSaveDTO, "admi@gmail.com"));
    }

    @Test
    void movieService_uploadFile_returnLinkFile() {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg",
                "some-image-content".getBytes());
        String expectedUrl = "http://example.com/upload/test.jpg";

        when(imgUtils.uploadFile(any(MultipartFile.class))).thenReturn(expectedUrl);

        String result = movieService.uploadFile(file);

        Assertions.assertEquals(expectedUrl, result);
    }
}