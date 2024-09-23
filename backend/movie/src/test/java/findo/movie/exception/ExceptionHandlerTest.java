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
import java.util.HashMap;
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

        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("message", "Duplicate title");

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleDuplicateTitleException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    void globarException_handleMovieNotFoundException_returnResponseEntity() {
        MovieNotFoundException ex = new MovieNotFoundException("Movie not found");

        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("message", "Movie not found");

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleMovieNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    void globarException_handleIllegalArgumentException_returnResponseEntity() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid argument");

        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("message", "Invalid argument");

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleIllegalArgumentException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    void globarException_handleGlobalException_returnResponseEntity() {
        Exception ex = new Exception("Generic error");

        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("message", "Generic error");

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleGlobalException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
    }
}
