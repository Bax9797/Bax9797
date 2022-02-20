package pl.kurs.java.test.expection.patient.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.kurs.java.test.expection.Errors.Error;
import pl.kurs.java.test.expection.patient.PatientNotFoundException;

@ControllerAdvice
public class PatientNotFoundHandler {

    @ExceptionHandler(value = PatientNotFoundException.class)
    public ResponseEntity<Object> handlerUserNotFoundException(PatientNotFoundException exception) {
        HttpStatus badRequest = HttpStatus.NOT_FOUND;
        Error error = new Error()
                .setHttpStatus(badRequest)
                .setStatus(badRequest.value())
                .setMessage(exception.getMessage());
        return new ResponseEntity<>(error, badRequest);
    }
}