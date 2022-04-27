package com.example.growingshop.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = StringContainValidator.class)
@Documented
public @interface StringContain {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    boolean checkUpperEn() default false;
    boolean checkLowerEn() default false;
    boolean checkKo() default false;
    boolean checkNumber() default false;
    String hasSpecialCharacter() default "";
}
