package pl.kurs.java.test.exception.patient;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AgeAnimalNegativeException extends RuntimeException {
    public AgeAnimalNegativeException(String message) { super(message);}
}
