package pl.kurs.java.test.expection.visit.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.kurs.java.test.expection.Errors.Error;
import pl.kurs.java.test.expection.visit.NotFoundFreeVisitAtGivenTimeException;

@RestControllerAdvice
public class NotFoundFreeVisitAtGivenTimeExceptionHandler {

    @ExceptionHandler(value = NotFoundFreeVisitAtGivenTimeException.class)
    public ResponseEntity<Object> handlerNotFoundFreeVisitAtGivenTimeException(NotFoundFreeVisitAtGivenTimeException exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        Error error = new Error()
                .setHttpStatus(badRequest)
                .setStatus(badRequest.value())
                .setMessage(exception.getMessage());
        return new ResponseEntity<>(error, badRequest);
    }
}
