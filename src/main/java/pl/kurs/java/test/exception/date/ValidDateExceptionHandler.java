package pl.kurs.java.test.exception.date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidDateExceptionHandler {
    @ExceptionHandler(value = ValidDateException.class)
    public ResponseEntity<Object> handlerUserNotFoundException(ValidDateException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
