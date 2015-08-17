package com.nixmash.springdata.jpa.model.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created with IntelliJ IDEA.
 * User: daveburke
 * Date: 6/2/15
 * Time: 11:34 AM
 */
@Pattern(regexp=".+@.+\\..+", message="{ExtendedEmailValidator.email}")
@Target( { METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface ExtendedEmailValidator {
    String message() default "{ExtendedEmailValidator.email}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}