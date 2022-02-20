package pl.kurs.java.test.expection.token;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TimeResponseException extends RuntimeException {
    public TimeResponseException(String message) {
        super(message);
    }
}
