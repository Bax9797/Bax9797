package pl.kurs.java.test.exception.errors;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ErrorNotFoundFreeVisitAtGivenTimeResponse {
    private String code;
    private String errorDateFrom;
    private String errorDateTo;
}
