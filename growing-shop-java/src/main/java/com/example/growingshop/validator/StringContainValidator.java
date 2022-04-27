package com.example.growingshop.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringContainValidator implements ConstraintValidator<StringContain, String> {
    private final Pattern UPPER_EN_REGEX = Pattern.compile("[A-Z]", Pattern.DOTALL);
    private final Pattern LOWER_EN_REGEX = Pattern.compile("[a-z]", Pattern.DOTALL);
    private final Pattern KO_REGEX = Pattern.compile("[가-힣]", Pattern.DOTALL);
    private final Pattern NUMBER_REGEX = Pattern.compile("[\\d]", Pattern.DOTALL);

    private final String INVALIDATION_PREFIX_MESSAGE = "다음 값이 필요합니다.\n";
    private final String UPPER_EN_INVALIDATION_MESSAGE = "영어 대문자";
    private final String LOWER_EN_INVALIDATION_MESSAGE = "영어 소문자";
    private final String KO_INVALIDATION_MESSAGE = "한글";
    private final String NUMBER_INVALIDATION_MESSAGE = "숫자";
    private final String SPECIAL_CHARACTER_INVALIDATION_MESSAGE = "특정 특수문자 ({0})";

    private boolean checkUpperEn;
    private boolean checkLowerEn;
    private boolean checkKo;
    private boolean checkNumber;
    private String hasSpecialCharacter;

    @Override
    public void initialize(StringContain constraintAnnotation) {
        this.checkUpperEn = constraintAnnotation.checkUpperEn();
        this.checkLowerEn = constraintAnnotation.checkLowerEn();
        this.checkKo = constraintAnnotation.checkKo();
        this.checkNumber = constraintAnnotation.checkNumber();
        this.hasSpecialCharacter = constraintAnnotation.hasSpecialCharacter();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        List<String> result = new ArrayList<>();

        checkUpperEn(value, result);
        checkLowerEn(value, result);
        checkKo(value, result);
        checkNumber(value, result);
        checkSpecialCharacter(value, result);

        if (!result.isEmpty()) {
            String message = INVALIDATION_PREFIX_MESSAGE + String.join(", ", result);
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();

            return false;
        }

        return true;
    }

    private void checkUpperEn(String value, List<String> result) {
        if (checkUpperEn && !UPPER_EN_REGEX.matcher(value).find()) {
            result.add(UPPER_EN_INVALIDATION_MESSAGE);
        }
    }

    private void checkLowerEn(String value, List<String> result) {
        if (checkLowerEn && !LOWER_EN_REGEX.matcher(value).find()) {
            result.add(LOWER_EN_INVALIDATION_MESSAGE);
        }
    }

    private void checkKo(String value, List<String> result) {
        if (checkKo && !KO_REGEX.matcher(value).find()) {
            result.add(KO_INVALIDATION_MESSAGE);
        }
    }

    private void checkNumber(String value, List<String> result) {
        if (checkNumber && !NUMBER_REGEX.matcher(value).find()) {
            result.add(NUMBER_INVALIDATION_MESSAGE);
        }
    }

    private void checkSpecialCharacter(String value, List<String> result) {
        Pattern p = Pattern.compile("[" + convertSpecialCharacterContainRegex() + "]", Pattern.DOTALL);

        if (!hasSpecialCharacter.isEmpty() && !p.matcher(value).find()) {
            result.add(SPECIAL_CHARACTER_INVALIDATION_MESSAGE.replace("{0}", hasSpecialCharacter));
        }
    }

    private String convertSpecialCharacterContainRegex() {
        return Arrays.stream(hasSpecialCharacter.split(""))
                .map((val) -> "\\" + val)
                .collect(Collectors.joining());
    }
}
