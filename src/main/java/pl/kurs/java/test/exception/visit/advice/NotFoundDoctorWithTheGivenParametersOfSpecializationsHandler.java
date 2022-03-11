package pl.kurs.java.test.exception.visit.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.kurs.java.test.exception.visit.NotFoundDoctorWithTheGivenParametersOfSpecializationsException;

@RestControllerAdvice
public class NotFoundDoctorWithTheGivenParametersOfSpecializationsHandler {

    @ExceptionHandler(value = NotFoundDoctorWithTheGivenParametersOfSpecializationsException.class)
    public ResponseEntity<Object> notFoundDoctorWithTheGivenParametersOfSpecializationsHandler(NotFoundDoctorWithTheGivenParametersOfSpecializationsException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
