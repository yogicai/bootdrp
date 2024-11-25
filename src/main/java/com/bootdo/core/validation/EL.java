package com.bootdo.core.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author L
 * @since 2024-10-14 16:58
 */
@Target({METHOD, FIELD, TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ELValidator.class)
@Documented
public @interface EL {

    String message() default "Invalid expression";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String expression() default "";

}
