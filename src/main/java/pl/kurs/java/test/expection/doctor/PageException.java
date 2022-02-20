package pl.kurs.java.test.expection.doctor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PageException extends RuntimeException{
    public PageException(String message) {super(message);
    }
}
