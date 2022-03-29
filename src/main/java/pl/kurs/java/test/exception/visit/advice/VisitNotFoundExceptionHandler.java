package pl.kurs.java.test.exception.visit.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.kurs.java.test.exception.errors.ErrorMessageResponse;
import pl.kurs.java.test.exception.visit.VisitNotFoundException;

@RestControllerAdvice
public class VisitNotFoundExceptionHandler {

    @ExceptionHandler(value = VisitNotFoundException.class)
    public ResponseEntity<Object> handlerVisitNotFoundException(VisitNotFoundException exception) {
        ErrorMessageResponse response = new ErrorMessageResponse().setErrorMessage(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
