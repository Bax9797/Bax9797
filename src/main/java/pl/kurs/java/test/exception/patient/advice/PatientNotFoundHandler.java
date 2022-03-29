package pl.kurs.java.test.exception.patient.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.kurs.java.test.exception.errors.ErrorMessageResponse;
import pl.kurs.java.test.exception.patient.PatientNotFoundException;

@ControllerAdvice
public class PatientNotFoundHandler {

    @ExceptionHandler(value = PatientNotFoundException.class)
    public ResponseEntity<Object> handlerUserNotFoundException(PatientNotFoundException exception) {
        ErrorMessageResponse response = new ErrorMessageResponse().setErrorMessage(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}