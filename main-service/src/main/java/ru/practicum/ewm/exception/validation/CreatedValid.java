package ru.practicum.ewm.exception.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.TYPE_USE)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = CheckDateValidator.class)
public @interface CreatedValid {
    String message() default "eventDate раньше чем текущий момент + 2 часа или равен null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
