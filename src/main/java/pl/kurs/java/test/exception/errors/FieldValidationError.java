package pl.kurs.java.test.exception.errors;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldValidationError {
    private String field;
    private String message;
}