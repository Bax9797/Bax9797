package pl.kurs.java.test.exception.errors;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ErrorMessageNotFoundParameters {
    private String code;
    private String medicalSpecializationError;
    private String animalSpecializationError;
}
