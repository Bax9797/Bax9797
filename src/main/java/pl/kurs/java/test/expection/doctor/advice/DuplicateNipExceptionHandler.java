package pl.kurs.java.test.expection.doctor.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.kurs.java.test.expection.Errors.Error;
import pl.kurs.java.test.expection.doctor.DuplicateNipException;

@ControllerAdvice
public class DuplicateNipExceptionHandler {

    @ExceptionHandler(value = DuplicateNipException.class)
    public ResponseEntity<Object> handlerDuplicateNipException(DuplicateNipException exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        Error error = new Error()
                .setHttpStatus(badRequest)
                .setStatus(badRequest.value())
                .setMessage(exception.getMessage());
        return new ResponseEntity<>(error, badRequest);
    }
}
