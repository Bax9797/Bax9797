package pl.kurs.java.test.annotation;

import pl.kurs.java.test.annotation.validators.EmailNotExistingValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailNotExistingValidator.class)
@Documented
public @interface EmailNotExisting {
    String message() default "Email is already taken";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
