package pl.kurs.java.test.exception.errors;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ErrorsResponse {
    private String code;
    private List<FieldValidationError> fieldErrors = new ArrayList<>();
}
