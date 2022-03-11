package pl.kurs.java.test.annotation.validators;

import pl.kurs.java.test.annotation.NotAllowedDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class NotAllowedDateValidator implements ConstraintValidator<NotAllowedDate, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime dateTime, ConstraintValidatorContext constraintValidatorContext) {
        return dateTime.isAfter(LocalDateTime.now());
    }
}
