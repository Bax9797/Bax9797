package pl.kurs.java.test.annotation.validators;

import lombok.RequiredArgsConstructor;
import pl.kurs.java.test.annotation.NipNotExisting;
import pl.kurs.java.test.repository.DoctorRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class NipNotExistingValidator implements ConstraintValidator<NipNotExisting, String> {
    private final DoctorRepository repository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !repository.existsByNip(value);
    }
}
