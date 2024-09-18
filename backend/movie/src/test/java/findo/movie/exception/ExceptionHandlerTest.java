package findo.movie.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ExceptionHandlerTest {
    
    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void globarException_handleValidationExceptions_returnResponseEntity() {
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("object", "field1", "Error message 1"));

        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(new ArrayList<>(fieldErrors));

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleValidationExceptions(methodArgumentNotValidException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Error message 1", response.getBody().get("field1"));
    }

    @Test
    void globarException_handleDuplicateTitleException_returnResponseEntity() {
        DuplicateTitleException ex = new DuplicateTitleException("Duplicate title");

        ResponseEntity<String> response = exceptionHandler.handleDuplicateTitleException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Duplicate title", response.getBody());
    }

    @Test
    void globarException_handleMovieNotFoundException_returnResponseEntity() {
        MovieNotFoundException ex = new MovieNotFoundException("Movie not found");

        ResponseEntity<String> response = exceptionHandler.handleMovieNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Movie not found", response.getBody());
    }

    @Test
    void globarException_handleIllegalArgumentException_returnResponseEntity() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid argument");

        ResponseEntity<String> response = exceptionHandler.handleIllegalArgumentException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid argument", response.getBody());
    }

    @Test
    void globarException_handleGlobalException_returnResponseEntity() {
        Exception ex = new Exception("Generic error");

        ResponseEntity<String> response = exceptionHandler.handleGlobalException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred: Generic error", response.getBody());
    }
}
