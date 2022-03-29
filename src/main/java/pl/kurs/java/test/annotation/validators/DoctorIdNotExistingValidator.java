package pl.kurs.java.test.annotation.validators;

import lombok.RequiredArgsConstructor;
import pl.kurs.java.test.annotation.DoctorIdNotExisting;
import pl.kurs.java.test.repository.DoctorRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class DoctorIdNotExistingValidator implements ConstraintValidator<DoctorIdNotExisting, Integer> {
    private final DoctorRepository repository;

    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext constraintValidatorContext) {
        return repository.existsById(id);
    }
}