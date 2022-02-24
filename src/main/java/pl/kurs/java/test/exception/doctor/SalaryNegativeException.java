package pl.kurs.java.test.exception.doctor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SalaryNegativeException extends RuntimeException {
    public SalaryNegativeException(String message) {
        super(message);
    }
}

