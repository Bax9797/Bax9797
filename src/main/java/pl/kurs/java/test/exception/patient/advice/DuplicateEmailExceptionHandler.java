package pl.kurs.java.test.exception.patient.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.kurs.java.test.exception.Errors.Error;
import pl.kurs.java.test.exception.patient.DuplicateEmailException;

@RestControllerAdvice
public class DuplicateEmailExceptionHandler {

    @ExceptionHandler(value = DuplicateEmailException.class)
    public ResponseEntity<Object> handlerDuplicateEmailException(DuplicateEmailException exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        Error error = new Error()
                .setHttpStatus(badRequest)
                .setStatus(badRequest.value())
                .setMessage(exception.getMessage());
        return new ResponseEntity<>(error, badRequest);
    }
}
