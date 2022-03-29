package pl.kurs.java.test.exception.visit;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VisitNotFoundException extends RuntimeException {
    public VisitNotFoundException() {
        super("There is no assigned visit with the given parameters");
    }
}
