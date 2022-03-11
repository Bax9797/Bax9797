package pl.kurs.java.test.annotation.validators;

import lombok.RequiredArgsConstructor;
import pl.kurs.java.test.annotation.EmailNotExisting;
import pl.kurs.java.test.repository.PatientRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class EmailNotExistingValidator implements ConstraintValidator<EmailNotExisting, String> {

    private final PatientRepository repository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return !repository.existsByEmail(value);
    }
}
