package pl.kurs.java.test.exception.doctor.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.kurs.java.test.exception.doctor.DoctorNotFoundException;

@ControllerAdvice
public class DoctorNotFoundExceptionHandler {

    @ExceptionHandler(value = DoctorNotFoundException.class)
    public ResponseEntity<Object> handlerUserNotFoundException(DoctorNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
