package com.example.growingshop.global.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = StringCheckerValidator.class)
@Documented
public @interface StringChecker {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    boolean includeUpperEn() default false;
    boolean includeLowerEn() default false;
    boolean includeKo() default false;
    boolean includeNumber() default false;
    String includeSpecialCharacter() default "";
}
