package pl.kurs.java.test.expection.patient.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.kurs.java.test.expection.Errors.Error;
import pl.kurs.java.test.expection.patient.AgeAnimalNegativeException;

@RestControllerAdvice
public class AgeAnimalNegativeExceptionHandler {

    @ExceptionHandler(value = AgeAnimalNegativeException.class)
    public ResponseEntity<Object> handlerAgeAnimalNegativeException(AgeAnimalNegativeException exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        Error error = new Error()
                .setHttpStatus(badRequest)
                .setStatus(badRequest.value())
                .setMessage(exception.getMessage());
        return new ResponseEntity<>(error, badRequest);
    }
}
