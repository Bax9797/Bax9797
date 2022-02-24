package pl.kurs.java.test.exception.date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.kurs.java.test.exception.Errors.Error;

@RestControllerAdvice
public class ValidDateExceptionHandler {
    @ExceptionHandler(value = ValidDateException.class)
    public ResponseEntity<Object> handlerUserNotFoundException(ValidDateException exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        Error error = new Error()
                .setHttpStatus(badRequest)
                .setStatus(badRequest.value())
                .setMessage(exception.getMessage());
        return new ResponseEntity<>(error, badRequest);
    }
}
