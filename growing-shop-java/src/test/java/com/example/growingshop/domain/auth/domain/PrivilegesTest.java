package com.example.growingshop.domain.auth.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class PrivilegesTest {

    @Test
    void 정책_목록의_path_포함여부_확인_테스트() {
        // given
        Privileges privileges = new Privileges(
                Arrays.asList(
                        Privilege.builder().path("/path1").build(),
                        Privilege.builder().path("/path2").build(),
                        Privilege.builder().path("/path3").build(),
                        Privilege.builder().path("/path4").build()
                )
        );

        // when
        boolean containPath = privileges.containPath("/path1");
        boolean notContainPath = privileges.containPath("/path5");

        // then
        assertThat(containPath).isTrue();
        assertThat(notContainPath).isFalse();
    }
}
