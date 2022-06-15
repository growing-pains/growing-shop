package com.example.growingshop.domain.auth.service;

import com.example.growingshop.domain.auth.dto.AuthRequest;
import com.example.growingshop.domain.user.domain.User;
import com.example.growingshop.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    private final String loginId = "loginId";
    private final String password = "password";
    private final String wrongPassword = "wrong" + password;

    @BeforeEach
    void setUp() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        when(passwordEncoder.encode(anyString()))
                .then(i -> encoder.encode(i.getArgument(0, String.class)));
        when(passwordEncoder.matches(anyString(), anyString()))
                .then(i -> encoder.matches(i.getArgument(0, String.class), i.getArgument(1, String.class)));
    }

    @Test
    void 시큐리티에_사용하는_패스워드_인코더의_encode_는_정상_동작해야_한다() {
        // given
        String encodedString = passwordEncoder.encode(password);

        // then
        assertThat(encodedString).isNotSameAs(password);
        assertThat(passwordEncoder.matches(password, encodedString)).isTrue();
    }

    @Test
    void 올바른_정보로_계정을_체크하면_하면_true_가_리턴되어야_한다() {
        // given
        User savedUser = User.builder()
                .loginId(loginId)
                .password(passwordEncoder.encode(password))
                .build();
        when(userRepository.findUsersByLoginId(loginId)).thenReturn(Optional.of(savedUser));

        // when
        AuthRequest.LoginReq login = AuthRequest.LoginReq.builder()
                .loginId(loginId)
                .password(password)
                .build();
        boolean result = authService.matchLoginUser(login);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 잘못된_정보로_계정을_체크하면_false_가_리턴되어야_한다() {
        // given
        User mockUser = User.builder()
                .loginId(loginId)
                .password(passwordEncoder.encode(password))
                .build();
        when(userRepository.findUsersByLoginId(loginId)).thenReturn(Optional.of(mockUser));

        // when
        AuthRequest.LoginReq login = AuthRequest.LoginReq.builder()
                .loginId(loginId)
                .password(wrongPassword)
                .build();
        boolean result = authService.matchLoginUser(login);

        // then
        assertThat(result).isFalse();
    }
}
