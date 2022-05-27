package com.example.growingshop.global.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringContainValidator implements ConstraintValidator<StringAnyContain, String> {
    private final Pattern UPPER_EN_REGEX = Pattern.compile("[A-Z]", Pattern.DOTALL);
    private final Pattern LOWER_EN_REGEX = Pattern.compile("[a-z]", Pattern.DOTALL);
    private final Pattern KO_REGEX = Pattern.compile("[가-힣]", Pattern.DOTALL);
    private final Pattern NUMBER_REGEX = Pattern.compile("[\\d]", Pattern.DOTALL);

    private final String INVALIDATION_PREFIX_MESSAGE = "다음값 중 하나 이상을 만족해야 합니다.\n";
    private final String UPPER_EN_INVALIDATION_MESSAGE = "영어 대문자";
    private final String LOWER_EN_INVALIDATION_MESSAGE = "영어 소문자";
    private final String KO_INVALIDATION_MESSAGE = "한글";
    private final String NUMBER_INVALIDATION_MESSAGE = "숫자";
    private final String SPECIAL_CHARACTER_INVALIDATION_MESSAGE = "특정 특수문자 ({0})";

    private boolean checkContainUpperEn;
    private boolean checkContainLowerEn;
    private boolean checkContainKo;
    private boolean checkContainNumber;
    private String hasContainSpecialCharacter;

    @Override
    public void initialize(StringAnyContain constraintAnnotation) {
        this.checkContainUpperEn = constraintAnnotation.checkContainUpperEn();
        this.checkContainLowerEn = constraintAnnotation.checkContainLowerEn();
        this.checkContainKo = constraintAnnotation.checkContainKo();
        this.checkContainNumber = constraintAnnotation.checkContainNumber();
        this.hasContainSpecialCharacter = constraintAnnotation.hasContainSpecialCharacter();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        List<String> result = new ArrayList<>();

        boolean validateResult = Stream.of(
                checkUpperEn(value, result),
                checkLowerEn(value, result),
                checkKo(value, result),
                checkNumber(value, result),
                checkSpecialCharacter(value, result)
        ).anyMatch(validate -> validate);

        if (!result.isEmpty()) {
            String message = INVALIDATION_PREFIX_MESSAGE + String.join(", ", result);
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();

            return false;
        }

        return true;
    }

    private boolean checkUpperEn(String value, List<String> result) {
        if (checkContainUpperEn && !UPPER_EN_REGEX.matcher(value).find()) {
            result.add(UPPER_EN_INVALIDATION_MESSAGE);
            return false;
        }
        return checkContainUpperEn;
    }

    private boolean checkLowerEn(String value, List<String> result) {
        if (checkContainLowerEn && !LOWER_EN_REGEX.matcher(value).find()) {
            result.add(LOWER_EN_INVALIDATION_MESSAGE);
            return false;
        }
        return checkContainLowerEn;
    }

    private boolean checkKo(String value, List<String> result) {
        if (checkContainKo && !KO_REGEX.matcher(value).find()) {
            result.add(KO_INVALIDATION_MESSAGE);
            return false;
        }
        return checkContainKo;
    }

    private boolean checkNumber(String value, List<String> result) {
        if (checkContainNumber && !NUMBER_REGEX.matcher(value).find()) {
            result.add(NUMBER_INVALIDATION_MESSAGE);
            return false;
        }
        return checkContainNumber;
    }

    private boolean checkSpecialCharacter(String value, List<String> result) {
        if (!hasContainSpecialCharacter.isEmpty() && !checkSpecialCharacterContain(value)) {
            result.add(SPECIAL_CHARACTER_INVALIDATION_MESSAGE.replace("{0}", hasContainSpecialCharacter));
            return false;
        }
        return hasContainSpecialCharacter.isEmpty();
    }

    private boolean checkSpecialCharacterContain(String value) {
        return Pattern.compile(Arrays.stream(hasContainSpecialCharacter.split(""))
                .collect(Collectors.joining("\\", "[\\", "]")))
                .matcher(value)
                .find();
    }
}
