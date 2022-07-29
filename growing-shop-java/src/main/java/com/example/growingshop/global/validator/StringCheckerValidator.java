package com.example.growingshop.global.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringCheckerValidator implements ConstraintValidator<StringChecker, String> {
    private final Pattern UPPER_EN_REGEX = Pattern.compile("[A-Z]", Pattern.DOTALL);
    private final Pattern LOWER_EN_REGEX = Pattern.compile("[a-z]", Pattern.DOTALL);
    private final Pattern KO_REGEX = Pattern.compile("[가-힣]", Pattern.DOTALL);
    private final Pattern NUMBER_REGEX = Pattern.compile("[\\d]", Pattern.DOTALL);

    private final String INCLUDE_PREFIX_MESSAGE = "다음값 중 하나 이상을 만족해야 합니다. ";
    private final String EXCLUDE_PREFIX_MESSAGE = "다음값은 포함되어선 안됩니다.";
    private final String UPPER_EN_INVALIDATION_MESSAGE = "영어 대문자";
    private final String LOWER_EN_INVALIDATION_MESSAGE = "영어 소문자";
    private final String KO_INVALIDATION_MESSAGE = "한글";
    private final String NUMBER_INVALIDATION_MESSAGE = "숫자";
    private final String SPECIAL_CHARACTER_INVALIDATION_MESSAGE = "특정 특수문자 ({0})";
    private final String SPECIAL_CHARACTER_DOES_NOT_INCLUDE_SUFFIX_MESSAGE = " 를 제외한 문자";

    private boolean includeUpperEn;
    private boolean includeLowerEn;
    private boolean includeKo;
    private boolean includeNumber;
    private String includeSpecialCharacter;
    private String target;
    private boolean includeValidateResult = false;
    private boolean excludeValidateResult = true;

    @Override
    public void initialize(StringChecker constraintAnnotation) {
        this.includeUpperEn = constraintAnnotation.includeUpperEn();
        this.includeLowerEn = constraintAnnotation.includeLowerEn();
        this.includeKo = constraintAnnotation.includeKo();
        this.includeNumber = constraintAnnotation.includeNumber();
        this.includeSpecialCharacter = constraintAnnotation.includeSpecialCharacter();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        this.target = value;

        String anyIncludeValidatorMessage = checkContainAnyInclude();
        String excludeValidatorMessage = checkContainExclude();

        if (!includeValidateResult || !excludeValidateResult) {
            String message = Stream.of(anyIncludeValidatorMessage, excludeValidatorMessage)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining("\n"));
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();

            return false;
        }

        return true;
    }

    private String checkContainAnyInclude() {
        List<String> message = Stream.of(
                checkIncludeUpperEn(),
                checkIncludeLowerEn(),
                checkIncludeKo(),
                checkIncludeNumber(),
                checkIncludeSpecialCharacter()
        ).filter(Objects::nonNull).collect(Collectors.toList());

        if (!includeValidateResult) {
            return INCLUDE_PREFIX_MESSAGE + String.join(", ", message);
        }
        return null;
    }

    private String checkContainExclude() {
        List<String> message = Stream.of(
                checkExcludeUpperEn(),
                checkExcludeLowerEn(),
                checkExcludeKo(),
                checkExcludeNumber(),
                checkExcludeSpecialCharacter()
        ).filter(Objects::nonNull).collect(Collectors.toList());

        if (!excludeValidateResult) {
            return EXCLUDE_PREFIX_MESSAGE + String.join(", ", message);
        }
        return null;
    }

    private String checkIncludeUpperEn() {
        if (includeUpperEn && !UPPER_EN_REGEX.matcher(this.target).find()) {
            return UPPER_EN_INVALIDATION_MESSAGE;
        }
        includeValidateResult = includeValidateResult || includeUpperEn;

        return null;
    }

    private String checkExcludeUpperEn() {
        if (!includeUpperEn && UPPER_EN_REGEX.matcher(this.target).find()) {
            this.excludeValidateResult = false;
            return UPPER_EN_INVALIDATION_MESSAGE;
        }

        return null;
    }

    private String checkIncludeLowerEn() {
        if (includeLowerEn && !LOWER_EN_REGEX.matcher(this.target).find()) {
            return LOWER_EN_INVALIDATION_MESSAGE;
        }
        includeValidateResult = includeValidateResult || includeLowerEn;

        return null;
    }

    private String checkExcludeLowerEn() {
        if (!includeLowerEn && LOWER_EN_REGEX.matcher(this.target).find()) {
            this.excludeValidateResult = false;
            return LOWER_EN_INVALIDATION_MESSAGE;
        }

        return null;
    }

    private String checkIncludeKo() {
        if (includeKo && !KO_REGEX.matcher(this.target).find()) {
            return KO_INVALIDATION_MESSAGE;
        }
        includeValidateResult = includeValidateResult || includeKo;

        return null;
    }

    private String checkExcludeKo() {
        if (!includeKo && KO_REGEX.matcher(this.target).find()) {
            this.excludeValidateResult = false;
            return KO_INVALIDATION_MESSAGE;
        }

        return null;
    }

    private String checkIncludeNumber() {
        if (includeNumber && !NUMBER_REGEX.matcher(this.target).find()) {
            return NUMBER_INVALIDATION_MESSAGE;
        }
        includeValidateResult = includeValidateResult || includeNumber;

        return null;
    }

    private String checkExcludeNumber() {
        if (!includeNumber && NUMBER_REGEX.matcher(this.target).find()) {
            this.excludeValidateResult = false;
            return NUMBER_INVALIDATION_MESSAGE;
        }

        return null;
    }

    private String checkIncludeSpecialCharacter() {
        if (!includeSpecialCharacter.isEmpty() && !checkSpecialCharacterContain(this.target)) {
            return SPECIAL_CHARACTER_INVALIDATION_MESSAGE.replace("{0}", includeSpecialCharacter);
        }
        includeValidateResult = includeValidateResult || !includeSpecialCharacter.isEmpty();

        return null;
    }

    private String checkExcludeSpecialCharacter() {
        boolean hasExcludeSpecialCharacter = !this.target.replaceAll(UPPER_EN_REGEX.toString(), "")
                .replaceAll(LOWER_EN_REGEX.toString(), "")
                .replaceAll(KO_REGEX.toString(), "")
                .replaceAll(NUMBER_REGEX.toString(), "")
                .replaceAll(specialCharacterRegex(), "")
                .isEmpty();

        if (hasExcludeSpecialCharacter) {
            this.excludeValidateResult = false;
            return SPECIAL_CHARACTER_INVALIDATION_MESSAGE.replace("{0}", includeSpecialCharacter)
                    + SPECIAL_CHARACTER_DOES_NOT_INCLUDE_SUFFIX_MESSAGE;
        }

        return null;
    }

    private String specialCharacterRegex() {
        if (includeSpecialCharacter.isEmpty()) {
            return "";
        }
        return Arrays.stream(includeSpecialCharacter.split(""))
                .collect(Collectors.joining("\\", "[\\", "]"));
    }

    private boolean checkSpecialCharacterContain(String value) {
        return Pattern.compile(specialCharacterRegex())
                .matcher(value)
                .find();
    }
}
