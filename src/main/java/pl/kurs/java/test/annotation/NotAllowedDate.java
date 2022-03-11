package pl.kurs.java.test.annotation;

import pl.kurs.java.test.annotation.validators.NotAllowedDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotAllowedDateValidator.class)
@Documented
public @interface NotAllowedDate {
    String message() default " date must be in the future";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
