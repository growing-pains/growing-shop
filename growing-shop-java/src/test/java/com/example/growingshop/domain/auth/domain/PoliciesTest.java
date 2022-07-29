package com.example.growingshop.domain.auth.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class PoliciesTest {

    @Test
    void 정책_목록의_path_포함여부_확인_테스트() {
        // given
        Policies policies = new Policies(
                Arrays.asList(
                        Policy.builder().path("/path1").build(),
                        Policy.builder().path("/path2").build(),
                        Policy.builder().path("/path3").build(),
                        Policy.builder().path("/path4").build()
                )
        );

        // when
        boolean containPath = policies.containPath("/path1");
        boolean notContainPath = policies.containPath("/path5");

        // then
        assertThat(containPath).isTrue();
        assertThat(notContainPath).isFalse();
    }
}
