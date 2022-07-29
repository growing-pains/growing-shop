package com.example.growingshop.domain.auth.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

class PrivilegeTest {

    @Test
    void 권한_생성시_url_path_가_유효하지_않으면_예외가_발생해야_한다() {
        assertThatIllegalArgumentException().isThrownBy(() -> Privilege.builder().path("invalid-path"));
    }

    @Test
    void 권한_생성시_유효한_url_path_로_생성하면_정상_생성되어야_한다() {
        assertThatNoException().isThrownBy(() -> Privilege.builder().path("/valid-path"));
        assertThatNoException().isThrownBy(() -> Privilege.builder().path("/valid_path"));
        assertThatNoException().isThrownBy(() -> Privilege.builder().path("/validPath"));
    }
}
