package pl.kurs.java.test.exception.visit;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotFoundFreeVisitAtGivenTimeException extends RuntimeException {
    public NotFoundFreeVisitAtGivenTimeException(String message) {super(message);}
}
