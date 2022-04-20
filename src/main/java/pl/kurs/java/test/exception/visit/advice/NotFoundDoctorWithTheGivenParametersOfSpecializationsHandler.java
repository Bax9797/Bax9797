package pl.kurs.java.test.exception.visit.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.kurs.java.test.exception.errors.ErrorMessageNotFoundParameters;
import pl.kurs.java.test.exception.visit.NotFoundDoctorWithTheGivenParametersOfSpecializationsException;

@RestControllerAdvice
public class NotFoundDoctorWithTheGivenParametersOfSpecializationsHandler {

    @ExceptionHandler(value = NotFoundDoctorWithTheGivenParametersOfSpecializationsException.class)
    public ResponseEntity<Object> notFoundDoctorWithTheGivenParametersOfSpecializationsHandler(NotFoundDoctorWithTheGivenParametersOfSpecializationsException exception) {
        ErrorMessageNotFoundParameters response = new ErrorMessageNotFoundParameters()
                .setCode("NOT_FOUND_WITH_GIVEN_PARAMETERS")
                .setMedicalSpecializationError(exception.getMedicalSpecialization())
                .setAnimalSpecializationError(exception.getAnimalSpecialization());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
