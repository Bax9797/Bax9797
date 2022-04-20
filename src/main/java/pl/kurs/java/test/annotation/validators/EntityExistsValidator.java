package pl.kurs.java.test.annotation.validators;

import lombok.RequiredArgsConstructor;
import pl.kurs.java.test.annotation.EntityExists;
import pl.kurs.java.test.annotation.EntityExistsFacade;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class EntityExistsValidator implements ConstraintValidator<EntityExists, Integer> {
    private final EntityExistsFacade entityExistsFacade;
    private Class type;

    @Override
    public void initialize(EntityExists constraintAnnotation) {
        this.type = constraintAnnotation.type();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return entityExistsFacade.validation(integer, type);
    }
}
