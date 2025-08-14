package gblas.books.backend.exceptions;

import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            BadRequestException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ErrorDetails> handleBadRequest(Exception ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorDetails> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDetails> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        Throwable cause = ex.getMostSpecificCause();
        String message;

        if (cause instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException invalidFormatEx) {
            message = "Invalid value for field: " + invalidFormatEx.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .reduce((a, b) -> a + "." + b)
                    .orElse("unknown");
        }
        else {
            message = "Request body is missing or malformed";
        }

        return buildErrorResponse(new RuntimeException(message), request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> handleUserAlreadyExists(UserAlreadyExistsException ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> handleResourceAlreadyExists(ResourceAlreadyExistsException ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDetails> handleNotFound(NotFoundException ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDetails> handleResponseStatus(ResponseStatusException ex, WebRequest request) {
        return buildErrorResponse(new Exception(ex.getReason()), request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleValidationErrors(MethodArgumentNotValidException ex, WebRequest request) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return buildErrorResponse(
                new Exception(errorMessage),
                request,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDetails> handleConstraintViolations(ConstraintViolationException ex, WebRequest request) {
        String errorMessage = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining("; "));

        return buildErrorResponse(
                new Exception(errorMessage),
                request,
                HttpStatus.BAD_REQUEST
        );
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDetails> handleTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String msg;
        if (ex.getRequiredType() == UUID.class) {
            msg = "Invalid UUID format: " + ex.getValue();
        } else {
            msg = "Invalid parameter: " + ex.getName();
        }
        return buildErrorResponse(new IllegalArgumentException(msg), request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleAllExceptions(Exception ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorDetails> buildErrorResponse(Exception ex, WebRequest request, HttpStatus status) {
        log.warn("Handled {}: {}", ex.getClass().getSimpleName(), ex.getMessage());
        ErrorDetails errorDetails = new ErrorDetails(
                status.value(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, status);
    }
}
