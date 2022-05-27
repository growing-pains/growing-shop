package com.example.growingshop.domain.auth.service;

import com.example.growingshop.domain.auth.dto.AuthRequest;
import com.example.growingshop.domain.user.domain.User;
import com.example.growingshop.domain.user.repository.UserRepository;
import com.example.growingshop.domain.user.service.UserService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthServiceTest {

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private AuthService authService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    private final String loginId = "loginId";
    private final String password = "password";
    private final String wrongPassword = "wrong" + password;
    private final String encodeSuffix = "_encode";

    @Test
    void 시큐리티에_사용하는_패스워드_인코더의_encode_는_정상_동작해야_한다() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedString = passwordEncoder.encode(password);

        assertThat(encodedString).isNotSameAs(password);
        assertThat(passwordEncoder.matches(password, encodedString)).isTrue();
    }

    @Test
    void 올바른_정보로_계정을_체크하면_하면_true_가_리턴되어야_한다() throws IllegalAccessException {
        when(userRepository.findUsersByLoginId(loginId))
                .thenReturn(Optional.of(User.builder().loginId(loginId).password(password + encodeSuffix).build()));
        when(passwordEncoder.encode(password)).thenReturn(password + encodeSuffix);
        when(userRepository.save(any())).then(i -> i.getArgument(0, User.class));
        when(passwordEncoder.matches(anyString(), anyString())).then(i -> i.getArgument(0, String.class).equals(i.getArgument(1, String.class)));

        AuthRequest.JoinReq join = AuthRequest.JoinReq.builder()
                .loginId(loginId)
                .password(password)
                .build();
        AuthRequest.LoginReq login = AuthRequest.LoginReq.builder()
                .loginId(loginId)
                .password(password)
                .build();
        userService.joinUser(join);

        boolean result = authService.matchLoginUser(login);

        assertThat(result).isTrue();
    }

    @Test
    void 잘못된_정보로_계정을_체크하면_false_가_리턴되어야_한다() throws IllegalAccessException {
        User mockUser = User.builder()
                .loginId(loginId)
                .password(passwordEncoder.encode(password))
                .build();
        when(userRepository.findUsersByLoginId(loginId)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any())).then(i -> i.getArgument(0, User.class));

        AuthRequest.JoinReq join = AuthRequest.JoinReq.builder()
                .loginId(loginId)
                .password(password)
                .mobile("")
                .build();
        AuthRequest.LoginReq login = AuthRequest.LoginReq.builder()
                .loginId(loginId)
                .password(wrongPassword)
                .build();
        userService.joinUser(join);

        boolean result = authService.matchLoginUser(login);

        assertThat(result).isFalse();
    }
}
