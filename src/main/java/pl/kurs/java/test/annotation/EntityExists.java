package pl.kurs.java.test.annotation;

import pl.kurs.java.test.annotation.validators.EntityExistsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EntityExistsValidator.class)
public @interface EntityExists {
    String message() default "ERROR_ENTITY_EXISTS";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class type();

}
