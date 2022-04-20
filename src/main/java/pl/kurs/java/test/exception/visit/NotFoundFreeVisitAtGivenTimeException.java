package pl.kurs.java.test.exception.visit;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotFoundFreeVisitAtGivenTimeException extends RuntimeException {
    private String dateFrom;
    private String dateTo;

    public NotFoundFreeVisitAtGivenTimeException(LocalDateTime dateFrom, LocalDateTime dateTo) {
        this.dateFrom = String.valueOf(dateFrom);
        this.dateTo = String.valueOf(dateTo);
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }
}
