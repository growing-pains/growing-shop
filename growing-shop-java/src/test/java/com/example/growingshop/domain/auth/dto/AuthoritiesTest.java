package com.example.growingshop.domain.auth.dto;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class AuthoritiesTest {

    @Test
    void 권한_목록의_path_허용여부는_목록의_path_가_포함되어있는_경로만_허용되어야_한다() {
        // given
        Authorities authorities = new Authorities(
                Arrays.asList(
                        new Authority("ROLE_1", Arrays.asList("/path-1_1", "/path-1_2", "/path-2_1")),
                        new Authority("ROLE_2", Arrays.asList("/path-2_1", "/path-2_2", "/path-3_1")),
                        new Authority("ROLE_3", Arrays.asList("/path-3_1", "/path-3_2", "/path-1_1"))
                )
        );

        // when
        boolean allowPath = authorities.isAllowAccessPath("/path-1_1");
        boolean notAllowPath = authorities.isAllowAccessPath("/path-4_1");

        // then
        assertThat(allowPath).isTrue();
        assertThat(notAllowPath).isFalse();
    }
}
