package findo.movie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String MESSAGE_KEY = "message";

    // Handle validation errors (e.g., @NotBlank, @NotNull)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // Handle duplicate movie title
    @ExceptionHandler(DuplicateTitleException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateTitleException(DuplicateTitleException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put(MESSAGE_KEY, ex.getMessage());
        
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    // Handle movie not found
    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleMovieNotFoundException(MovieNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put(MESSAGE_KEY, ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Handle non-image file uploads
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put(MESSAGE_KEY, ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Handle any other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGlobalException(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put(MESSAGE_KEY, ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
