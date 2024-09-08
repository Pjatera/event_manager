package ru.javacourse.eventmanagement.exeption;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public final class GlobalExceptionHandler {
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, IllegalArgumentException.class, NullPointerException.class, ConstraintViolationException.class})
    public ResponseEntity<CustomErrorMessage> mismatchException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CustomErrorMessage("Некорректный запрос", ex.getMessage(), LocalDateTime.now()));
    }
    @ExceptionHandler(NotFoundLocation.class)
    public ResponseEntity<CustomErrorMessage> notFoundPetException(NotFoundLocation ex) {
        log.error(ex.getMessage(),ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new CustomErrorMessage("Сущность не найдена", ex.getMessage(), LocalDateTime.now()));
    }

}