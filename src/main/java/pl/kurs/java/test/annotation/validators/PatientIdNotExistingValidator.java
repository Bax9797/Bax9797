package pl.kurs.java.test.annotation.validators;

import lombok.RequiredArgsConstructor;
import pl.kurs.java.test.annotation.PatientIdNotExisting;
import pl.kurs.java.test.repository.PatientRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class PatientIdNotExistingValidator implements ConstraintValidator<PatientIdNotExisting, Integer> {

    private final PatientRepository repository;

    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext constraintValidatorContext) {
        return repository.existsById(id);
    }
}
