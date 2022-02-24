package pl.kurs.java.test.exception.doctor.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.kurs.java.test.exception.Errors.Error;
import pl.kurs.java.test.exception.doctor.PageException;

@RestControllerAdvice
public class PageExceptionHandler {

    @ExceptionHandler(value = PageException.class)
    public ResponseEntity<Object> handlerPageException(PageException exception) {
        HttpStatus badRequest = HttpStatus.NOT_FOUND;
        Error error = new Error()
                .setHttpStatus(badRequest)
                .setStatus(badRequest.value())
                .setMessage(exception.getMessage());
        return new ResponseEntity<>(error, badRequest);
    }
}
