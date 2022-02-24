package pl.kurs.java.test.exception.visit.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.kurs.java.test.exception.Errors.Error;
import pl.kurs.java.test.exception.visit.NotFoundDoctorWithTheGivenParametersOfSpecializationsException;

@RestControllerAdvice
public class NotFoundDoctorWithTheGivenParametersOfSpecializationsHandler {

    @ExceptionHandler(value = NotFoundDoctorWithTheGivenParametersOfSpecializationsException.class)
    public ResponseEntity<Object> notFoundDoctorWithTheGivenParametersOfSpecializationsHandler(NotFoundDoctorWithTheGivenParametersOfSpecializationsException exception) {
        HttpStatus badRequest = HttpStatus.NOT_FOUND;
        Error error = new Error()
                .setHttpStatus(badRequest)
                .setStatus(badRequest.value())
                .setMessage(exception.getMessage());
        return new ResponseEntity<>(error, badRequest);
    }
}
