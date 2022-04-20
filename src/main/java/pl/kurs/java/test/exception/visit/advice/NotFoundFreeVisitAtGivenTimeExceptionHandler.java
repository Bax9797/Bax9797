package pl.kurs.java.test.exception.visit.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.kurs.java.test.exception.errors.ErrorNotFoundFreeVisitAtGivenTimeResponse;
import pl.kurs.java.test.exception.visit.NotFoundFreeVisitAtGivenTimeException;

@RestControllerAdvice
public class NotFoundFreeVisitAtGivenTimeExceptionHandler {

    @ExceptionHandler(value = NotFoundFreeVisitAtGivenTimeException.class)
    public ResponseEntity<Object> handlerNotFoundFreeVisitAtGivenTimeException(NotFoundFreeVisitAtGivenTimeException exception) {
        ErrorNotFoundFreeVisitAtGivenTimeResponse response = new ErrorNotFoundFreeVisitAtGivenTimeResponse()
                .setCode("NOT_FOUND_FREE_VISIT_AT_GIVEN_TIME_EXCEPTION")
                .setErrorDateFrom(exception.getDateFrom())
                .setErrorDateTo(exception.getDateTo());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
