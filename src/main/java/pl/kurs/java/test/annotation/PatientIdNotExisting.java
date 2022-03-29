package pl.kurs.java.test.annotation;

import pl.kurs.java.test.annotation.validators.PatientIdNotExistingValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PatientIdNotExistingValidator.class)
@Documented
public @interface PatientIdNotExisting {
    String message() default "Patient not found with given id";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
