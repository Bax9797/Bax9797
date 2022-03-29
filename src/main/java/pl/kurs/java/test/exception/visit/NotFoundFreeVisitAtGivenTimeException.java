package pl.kurs.java.test.exception.visit;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotFoundFreeVisitAtGivenTimeException extends RuntimeException {
    public NotFoundFreeVisitAtGivenTimeException() {
        super("There is no free visit at given time, please change the time slot for the meeting");
    }
}
