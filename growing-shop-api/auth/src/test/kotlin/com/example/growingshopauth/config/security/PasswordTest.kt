package com.example.growingshopauth.config.security

import com.example.domain.user.User
import com.example.domain.user.UserGrade
import com.example.domain.user.UserStatus
import com.example.domain.user.UserType
import com.example.growingshopauth.auth.dto.AuthRequest
import com.example.growingshopauth.auth.service.AuthService
import com.example.growingshopauth.user.repository.ExpandUserRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.invocation.InvocationOnMock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

@ExtendWith(MockitoExtension::class)
class PasswordTest {

    @Mock
    lateinit var passwordEncoder: PasswordEncoder

    @Mock
    lateinit var userRepository: ExpandUserRepository

    @InjectMocks
    lateinit var authService: AuthService

    private val loginId = "loginId"
    private val password = "password"
    private val wrongPassword = "wrong_$password"

    @BeforeEach
    fun setUp() {
        val encoder: PasswordEncoder = BCryptPasswordEncoder()
        `when`(passwordEncoder.encode(ArgumentMatchers.anyString()))
            .then { i: InvocationOnMock ->
                encoder.encode(
                    i.getArgument(
                        0,
                        String::class.java
                    )
                )
            }
        `when`(passwordEncoder.matches(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
            .then { i: InvocationOnMock ->
                encoder.matches(
                    i.getArgument(0, String::class.java),
                    i.getArgument(
                        1,
                        String::class.java
                    )
                )
            }
    }

    @Test
    fun `시큐리티에 사용하는 패스워드 인코더의 encode 는 정상 동작해야 한다`() {
        // given
        val encodedString = passwordEncoder.encode(password)

        // then
        Assertions.assertThat(encodedString).isNotSameAs(password)
        Assertions.assertThat(passwordEncoder.matches(password, encodedString)).isTrue
    }

    @Test
    fun `올바른 정보로 계정을 체크하면 하면 true 가 리턴되어야 한다`() {
        // given
        val savedUser = User(
            loginId = loginId,
            password = passwordEncoder.encode(password),
            name = "",
            mobile = "",
            email = "",
            status = UserStatus.NORMAL,
            type = UserType.NORMAL,
            grade = UserGrade.NORMAL,
        )
        `when`(userRepository.findUsersByLoginId(loginId)).thenReturn(Optional.of(savedUser))

        // when
        val login: AuthRequest.LoginReq = AuthRequest.LoginReq(
            loginId = loginId,
            password = password
        )
        val result: Boolean = authService.matchLoginUser(login)

        // then
        Assertions.assertThat(result).isTrue
    }

    @Test
    fun `잘못된 정보로 계정을 체크하면 false 가 리턴되어야 한다`() {
        // given
        val mockUser = User(
            loginId = loginId,
            password = passwordEncoder.encode(password),
            name = "",
            mobile = "",
            email = "",
            status = UserStatus.NORMAL,
            type = UserType.NORMAL,
            grade = UserGrade.NORMAL,
        )
        `when`(userRepository.findUsersByLoginId(loginId)).thenReturn(Optional.of(mockUser))

        // when
        val login: AuthRequest.LoginReq = AuthRequest.LoginReq(
            loginId = loginId,
            password = wrongPassword
        )
        val result: Boolean = authService.matchLoginUser(login)

        // then
        Assertions.assertThat(result).isFalse
    }
}
