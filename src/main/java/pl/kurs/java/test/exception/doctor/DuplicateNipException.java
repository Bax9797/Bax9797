package pl.kurs.java.test.exception.doctor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicateNipException extends RuntimeException {
    public DuplicateNipException(String message) {
        super(message);
    }
}
