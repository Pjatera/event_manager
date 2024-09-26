package ru.javacourse.eventmanagement.domain.exeptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class, ConstraintViolationException.class})
    public ResponseEntity<CustomErrorMessage> mismatchException(RuntimeException ex) {
        log.error("Bad request exception", ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CustomErrorMessage("Bad request", ex.getMessage(), LocalDateTime.now()));
    }


    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<CustomErrorMessage> notFoundException(EntityNotFoundException ex) {
        log.error("Entity not found exception", ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new CustomErrorMessage("Entity not found", ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CustomErrorMessage> badCredentialsException(BadCredentialsException ex) {
        log.error("Failed to authenticate", ex);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new CustomErrorMessage("Bad credential", ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorMessage> exception(Exception ex) {
        log.error("Internal Server Error", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CustomErrorMessage("Internal Server Error", ex.getMessage(), LocalDateTime.now()));
    }

}
