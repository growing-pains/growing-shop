package com.example.growingshop.validator;

import com.example.growingshop.global.validator.StringAnyContain;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class StringAnyContainTest {
    private final static int NUMBER_START_NUMBER = 48;      // 0
    private final static int NUMBER_END_NUMBER = 57;        // 9
    private final static int UPPER_EN_START_NUMBER = 65;    // A
    private final static int UPPER_EN_END_NUMBER = 90;      // Z
    private final static int LOWER_EN_START_NUMBER = 97;    // a
    private final static int LOWER_EN_END_NUMBER = 122;     // z
    private final static int KO_START_NUMBER = 44032;       // 가
    private final static int KO_END_NUMBER = 55203;         // 힣
    private final static String SPECIAL_CHARACTER = "[]()-_.";

    private final static String NUMBER_CONTAIN_MESSAGE = "숫자";
    private final static String UPPER_EN_CONTAIN_MESSAGE = "영어 대문자";
    private final static String LOWER_EN_CONTAIN_MESSAGE = "영어 소문자";
    private final static String KO_CONTAIN_MESSAGE = "한글";
    private final static String SPECIAL_CHARACTER_CONTAIN_MESSAGE = "특정 특수문자";

    private final static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final static Random random = new Random();

    @Test
    void 대문자_포함_여부_테스트() {
        ContainUpper validObject = new ContainUpper(
                combineAndShuffle(
                        generateRandomUpperEn(),
                        generateRandomLowerEn(),
                        generateRandomKo(),
                        generateRandomNumber(),
                        generateRandomSpecialCharacter()
                )
        );
        ContainUpper invalidObject = new ContainUpper(
                combineAndShuffle(
                        generateRandomLowerEn(),
                        generateRandomKo(),
                        generateRandomNumber(),
                        generateRandomSpecialCharacter()
                )
        );

        Set<ConstraintViolation<ContainUpper>> validResult = validator.validate(validObject);
        Set<ConstraintViolation<ContainUpper>> invalidResult = validator.validate(invalidObject);

        assertThat(validResult.size()).isEqualTo(0);
        assertThat(invalidResult.size()).isEqualTo(1);
        invalidResult.forEach((value) -> assertThat(value.getMessage()).contains(UPPER_EN_CONTAIN_MESSAGE));
    }

    @Test
    void 소문자_포함_여부_테스트() {
        ContainLower validObject = new ContainLower(
                combineAndShuffle(
                        generateRandomUpperEn(),
                        generateRandomLowerEn(),
                        generateRandomKo(),
                        generateRandomNumber(),
                        generateRandomSpecialCharacter()
                )
        );
        ContainLower invalidObject = new ContainLower(
                combineAndShuffle(
                        generateRandomUpperEn(),
                        generateRandomKo(),
                        generateRandomNumber(),
                        generateRandomSpecialCharacter()
                )
        );

        Set<ConstraintViolation<ContainLower>> validResult = validator.validate(validObject);
        Set<ConstraintViolation<ContainLower>> invalidResult = validator.validate(invalidObject);

        assertThat(validResult.size()).isEqualTo(0);
        assertThat(invalidResult.size()).isEqualTo(1);
        invalidResult.forEach((value) -> assertThat(value.getMessage()).contains(LOWER_EN_CONTAIN_MESSAGE));
    }

    @Test
    void 한글_포함_여부_테스트() {
        ContainKo validObject = new ContainKo(
                combineAndShuffle(
                        generateRandomUpperEn(),
                        generateRandomLowerEn(),
                        generateRandomKo(),
                        generateRandomNumber(),
                        generateRandomSpecialCharacter()
                )
        );
        ContainKo invalidObject = new ContainKo(
                combineAndShuffle(
                        generateRandomUpperEn(),
                        generateRandomLowerEn(),
                        generateRandomNumber(),
                        generateRandomSpecialCharacter()
                )
        );

        Set<ConstraintViolation<ContainKo>> validResult = validator.validate(validObject);
        Set<ConstraintViolation<ContainKo>> invalidResult = validator.validate(invalidObject);

        assertThat(validResult.size()).isEqualTo(0);
        assertThat(invalidResult.size()).isEqualTo(1);
        invalidResult.forEach((value) -> assertThat(value.getMessage()).contains(KO_CONTAIN_MESSAGE));
    }

    @Test
    void 숫자_포함_여부_테스트() {
        ContainNumber validObject = new ContainNumber(
                combineAndShuffle(
                        generateRandomUpperEn(),
                        generateRandomLowerEn(),
                        generateRandomKo(),
                        generateRandomNumber(),
                        generateRandomSpecialCharacter()
                )
        );
        ContainNumber invalidObject = new ContainNumber(
                combineAndShuffle(
                        generateRandomUpperEn(),
                        generateRandomLowerEn(),
                        generateRandomKo(),
                        generateRandomSpecialCharacter()
                )
        );

        Set<ConstraintViolation<ContainNumber>> validResult = validator.validate(validObject);
        Set<ConstraintViolation<ContainNumber>> invalidResult = validator.validate(invalidObject);

        assertThat(validResult.size()).isEqualTo(0);
        assertThat(invalidResult.size()).isEqualTo(1);
        invalidResult.forEach((value) -> assertThat(value.getMessage()).contains(NUMBER_CONTAIN_MESSAGE));
    }

    @Test
    void 특수문자_포함_여부_테스트() {
        ContainSpecialCharacter validObject = new ContainSpecialCharacter(
                combineAndShuffle(
                        generateRandomUpperEn(),
                        generateRandomLowerEn(),
                        generateRandomKo(),
                        generateRandomNumber(),
                        generateRandomSpecialCharacter()
                )
        );
        ContainSpecialCharacter invalidObject = new ContainSpecialCharacter(
                combineAndShuffle(
                        generateRandomUpperEn(),
                        generateRandomLowerEn(),
                        generateRandomKo(),
                        generateRandomNumber()
                )
        );

        Set<ConstraintViolation<ContainSpecialCharacter>> validResult = validator.validate(validObject);
        Set<ConstraintViolation<ContainSpecialCharacter>> invalidResult = validator.validate(invalidObject);

        assertThat(validResult.size()).isEqualTo(0);
        assertThat(invalidResult.size()).isEqualTo(1);
        invalidResult.forEach((value) -> assertThat(value.getMessage()).contains(SPECIAL_CHARACTER_CONTAIN_MESSAGE));
    }

    @Test
    void 복합_문자_포함_테스트() {
        ContainAnyComplexString validObject = new ContainAnyComplexString(
                combineAndShuffle(
                        generateRandomUpperEn(),
                        generateRandomLowerEn(),
                        generateRandomKo(),
                        generateRandomNumber(),
                        generateRandomSpecialCharacter()
                )
        );
        ContainAnyComplexString invalidObject = new ContainAnyComplexString(
                combineAndShuffle(
                        generateRandomLowerEn(),
                        generateRandomKo()
                )
        );


        Set<ConstraintViolation<ContainAnyComplexString>> validResult = validator.validate(validObject);
        Set<ConstraintViolation<ContainAnyComplexString>> invalidResult = validator.validate(invalidObject);

        assertThat(validResult.size()).isEqualTo(0);
        assertThat(invalidResult.size()).isEqualTo(1);
        invalidResult.forEach((value) -> {
            List<String> messages = Arrays.asList(UPPER_EN_CONTAIN_MESSAGE, NUMBER_CONTAIN_MESSAGE, SPECIAL_CHARACTER_CONTAIN_MESSAGE);

            for (String message : messages) {
                assertThat(value.getMessage()).contains(message);
            }
        });
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

    private static class ContainUpper {
        @StringAnyContain(checkContainUpperEn = true)
        private final String containUpperEn;

        public ContainUpper(String containUpperEn) {
            this.containUpperEn = containUpperEn;
        }
    }

    private static class ContainLower {
        @StringAnyContain(checkContainLowerEn = true)
        private final String containLowerEn;

        public ContainLower(String containLowerEn) {
            this.containLowerEn = containLowerEn;
        }
    }

    private static class ContainKo {
        @StringAnyContain(checkContainKo = true)
        private final String containKo;

        public ContainKo(String containKo) {
            this.containKo = containKo;
        }
    }

    private static class ContainNumber {
        @StringAnyContain(checkContainNumber = true)
        private final String containNumber;

        public ContainNumber(String containNumber) {
            this.containNumber = containNumber;
        }
    }

    private static class ContainSpecialCharacter {
        @StringAnyContain(hasContainSpecialCharacter = SPECIAL_CHARACTER)
        private final String containSpecialCharacter;

        public ContainSpecialCharacter(String containSpecialCharacter) {
            this.containSpecialCharacter = containSpecialCharacter;
        }
    }

    private static class ContainAnyComplexString {
        @StringAnyContain(checkContainUpperEn = true, checkContainNumber = true, hasContainSpecialCharacter = SPECIAL_CHARACTER)
        private final String complexValue;

        private ContainAnyComplexString(String complexValue) {
            this.complexValue = complexValue;
        }
    }
}
