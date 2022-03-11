package pl.kurs.java.test.exception.token.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.kurs.java.test.exception.token.TokenNotFoundException;

@RestControllerAdvice
public class TokenNotFoundExceptionHandler {

    @ExceptionHandler(value = TokenNotFoundException.class)
    public ResponseEntity<Object> handlerTokenNotFoundException(TokenNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
