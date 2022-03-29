package pl.kurs.java.test.exception.errors;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ErrorMessageResponse {
    private String errorMessage;
}
