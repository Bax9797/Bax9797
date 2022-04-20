package pl.kurs.java.test.annotation;


import pl.kurs.java.test.annotation.validators.NipNotExistingValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NipNotExistingValidator.class)
@Documented
public @interface NipNotExisting {
    String message() default "ERROR_NIP_EXISTS";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
