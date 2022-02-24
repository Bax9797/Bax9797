package pl.kurs.java.test.exception.doctor.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.kurs.java.test.exception.Errors.Error;
import pl.kurs.java.test.exception.doctor.EmptyFieldsException;

@ControllerAdvice
public class EmptyFieldsExceptionHandler {

    @ExceptionHandler(value = EmptyFieldsException.class)
    public ResponseEntity<Object> handlerEmptyFieldsException(EmptyFieldsException exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        Error error = new Error()
                .setHttpStatus(badRequest)
                .setStatus(badRequest.value())
                .setMessage(exception.getMessage());
        return new ResponseEntity<>(error, badRequest);
    }
}
