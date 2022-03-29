package pl.kurs.java.test.exception.errors;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ErrorsResponse {
    private List<FieldValidationError> fieldErrors = new ArrayList<>();
}
