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
@Constraint(validatedBy = StringContainValidator.class)
@Documented
public @interface StringAnyContain {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    boolean checkContainUpperEn() default false;
    boolean checkContainLowerEn() default false;
    boolean checkContainKo() default false;
    boolean checkContainNumber() default false;
    String hasContainSpecialCharacter() default "";
}
