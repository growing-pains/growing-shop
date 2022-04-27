package com.example.growingshop.validator;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.*;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

class StringContainTest {
    private final static int NUMBER_START_NUMBER = 48;
    private final static int NUMBER_END_NUMBER = 58;
    private final static int UPPER_EN_START_NUMBER = 65;
    private final static int UPPER_EN_END_NUMBER = 90;
    private final static int LOWER_EN_START_NUMBER = 97;
    private final static int LOWER_EN_END_NUMBER = 122;
    private final static int KO_START_NUMBER = 44032;
    private final static int KO_END_NUMBER = 55203;
    private final static String SPECIAL_CHARACTER = "[]()-_.";

    private final static String NUMBER_CONTAIN_MESSAGE = "숫자";
    private final static String UPPER_EN_CONTAIN_MESSAGE = "영어 대문자";
    private final static String LOWER_EN_CONTAIN_MESSAGE = "영어 소문자";
    private final static String KO_CONTAIN_MESSAGE = "한글";
    private final static String SPECIAL_CHARACTER_CONTAIN_MESSAGE = "특정 특수문자";

    private final static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private Random random = new Random();

    @Test
    void test() {
        System.out.println(Pattern.compile("[\\[\\]\\(\\)\\-\\_\\.]", Pattern.DOTALL).matcher("_(댁)CsKfYvdH(X.h(R컴)먴pD_쓜)좚텚팅쁽gw꼶ae땓RGQ-").find());
    }

    @Test
    void StringContain_Custom_Validation_동작_테스트() {
        TestObject noContainNumber = new TestObject(
            combineAndShuffle(
                generateRandomUpperEn(),
                generateRandomLowerEn(),
                generateRandomKo(),
                generateRandomSpecialCharacter()
            )
        );
        TestObject noContainUpperEn = new TestObject(
            combineAndShuffle(
                generateRandomNumber(),
                generateRandomLowerEn(),
                generateRandomKo(),
                generateRandomSpecialCharacter()
            )
        );
        TestObject noContainLowerEn = new TestObject(
            combineAndShuffle(
                generateRandomNumber(),
                generateRandomUpperEn(),
                generateRandomKo(),
                generateRandomSpecialCharacter()
            )
        );
        TestObject noContainKo = new TestObject(
            combineAndShuffle(
                generateRandomNumber(),
                generateRandomUpperEn(),
                generateRandomLowerEn(),
                generateRandomSpecialCharacter()
            )
        );
        TestObject noContainSpecialCharacter = new TestObject(
            combineAndShuffle(
                generateRandomNumber(),
                generateRandomUpperEn(),
                generateRandomLowerEn(),
                generateRandomKo()
            )
        );

        Set<ConstraintViolation<TestObject>> numberValidateResult = validator.validate(noContainNumber);
        Set<ConstraintViolation<TestObject>> upperEnValidateResult = validator.validate(noContainUpperEn);
        Set<ConstraintViolation<TestObject>> lowerEnValidateResult = validator.validate(noContainLowerEn);
        Set<ConstraintViolation<TestObject>> koValidateResult = validator.validate(noContainKo);
        Set<ConstraintViolation<TestObject>> specialCharacterValidateResult = validator.validate(noContainSpecialCharacter);

        numberValidateResult.forEach((value) -> assertThat(value.getMessage()).contains(NUMBER_CONTAIN_MESSAGE));
        upperEnValidateResult.forEach((value) -> assertThat(value.getMessage()).contains(UPPER_EN_CONTAIN_MESSAGE));
        lowerEnValidateResult.forEach((value) -> assertThat(value.getMessage()).contains(LOWER_EN_CONTAIN_MESSAGE));
        koValidateResult.forEach((value) -> assertThat(value.getMessage()).contains(KO_CONTAIN_MESSAGE));
        specialCharacterValidateResult.forEach((value) -> assertThat(value.getMessage()).contains(SPECIAL_CHARACTER_CONTAIN_MESSAGE));
    }

    private String generateRandomNumber() {
        return generateRandomString(NUMBER_START_NUMBER, NUMBER_END_NUMBER);
    }

    private String generateRandomUpperEn() {
        return generateRandomString(UPPER_EN_START_NUMBER, UPPER_EN_END_NUMBER);
    }

    private String generateRandomLowerEn() {
        return generateRandomString(LOWER_EN_START_NUMBER, LOWER_EN_END_NUMBER);
    }

    private String generateRandomKo() {
        return generateRandomString(KO_START_NUMBER, KO_END_NUMBER);
    }

    private String generateRandomSpecialCharacter() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(SPECIAL_CHARACTER.length());
            sb.append(SPECIAL_CHARACTER.charAt(index));
        }

        return sb.toString();
    }

    private String generateRandomString(int startCharNumber, int endCharNumber) {
        return random.ints(startCharNumber, endCharNumber)
                .limit(10)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private String combineAndShuffle(String... args) {
        List<String> result = Arrays.asList(String.join("", args).split(""));
        Collections.shuffle(result);

        return String.join("", result);
    }

    public static class TestObject {
        @StringContain(checkUpperEn = true, checkLowerEn = true, checkKo = true, checkNumber = true, hasSpecialCharacter = SPECIAL_CHARACTER)
        private final String test;

        public TestObject(String test) {
            this.test = test;
        }
    }
}
