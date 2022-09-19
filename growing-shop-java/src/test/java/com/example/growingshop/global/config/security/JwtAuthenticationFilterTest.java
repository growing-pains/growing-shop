package com.example.growingshop.global.config.security;

import com.example.growingshop.domain.auth.domain.*;
import com.example.growingshop.domain.auth.service.PolicyService;
import com.example.growingshop.domain.auth.service.RoleService;
import com.example.growingshop.domain.user.domain.User;
import com.example.growingshop.domain.user.domain.UserType;
import com.example.growingshop.domain.user.repository.UserRepository;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {
    private static final String BEARER = "Bearer ";
    private static MockedStatic<JwtTokenProvider> jwtTokenProvider;

    private UserRepository userRepository = mock(UserRepository.class);
    private PolicyService policyService = mock(PolicyService.class);
    private RoleService roleService = mock(RoleService.class);

    private HttpServletRequest request;
    private HttpServletResponse response;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String adminAllowPath = "/admin";
    private static final String sellerAllowPath = "/seller";
    private static final String normalAllowPath = "/normal";
    private static final String userTypeAllowPath = "/default";
    private static final String allAllowPath = "/all";

    private static final Policy adminPolicy = new Policy("admin", adminAllowPath, HttpMethod.ALL, "");
    private static final Policy sellerPolicy = new Policy("seller", sellerAllowPath, HttpMethod.GET, "");
    private static final Policy normalPolicy = new Policy("normal", normalAllowPath, HttpMethod.GET, "");
    private static final Policy userTypePolicy = new Policy("default", userTypeAllowPath, HttpMethod.GET, "");

    private static final Roles adminRole = new Roles(Collections.singletonList(
            new Role("ADMIN", Arrays.asList(adminPolicy, sellerPolicy, normalPolicy))
    ));
    private static final Roles sellerRole = new Roles(Collections.singletonList(
            new Role("SELLER", Arrays.asList(sellerPolicy, normalPolicy))
    ));
    private static final Roles normalRole = new Roles(Collections.singletonList(
            new Role("NORMAL", Collections.singletonList(normalPolicy))
    ));
    private static final Role userTypeRole = new Role("DEFAULT", Collections.singletonList(userTypePolicy));

    private static final User admin = User.builder().type(UserType.ADMIN).roles(adminRole).build();
    private static final User seller = User.builder().type(UserType.NORMAL).roles(sellerRole).build();
    private static final User normal = User.builder().type(UserType.SELLER).roles(normalRole).build();

    @BeforeAll
    static void beforeAll() {
        jwtTokenProvider = mockStatic(JwtTokenProvider.class);
    }

    @AfterAll
    static void afterAll() {
        jwtTokenProvider.close();
    }

    @BeforeEach
    void beforeEach() {
        request = spy(HttpServletRequest.class);
        response = new MockHttpServletResponse();

        MockitoAnnotations.openMocks(this);

        lenient().when(request.getHeader("Authorization")).thenReturn(BEARER + "token");
        when(policyService.findAll()).thenReturn(new Policies(Arrays.asList(adminPolicy, sellerPolicy, normalPolicy)));
        when(roleService.findByName(any())).thenReturn(userTypeRole);
    }

    @Test
    void role_에_없는_경로_요청시_권한과_상관없이_접근이_가능해야_한다() throws ServletException, IOException {
        // given
        when(request.getRequestURI()).thenReturn(allAllowPath);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, new MockFilterChain());

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_OK);
    }

    @ParameterizedTest
    @MethodSource("notAllowParameterizedTestData")
    void path_에_대한_권한이_없는_사용자가_요청하면_접근이_불가능해야_한다(User user) throws ServletException, IOException {
        // given
        when(JwtTokenProvider.getUserIdFromJwt(any())).thenReturn(user.getLoginId());
        when(userRepository.findUsersByLoginId(any())).thenReturn(Optional.of(user));
        when(request.getRequestURI()).thenReturn(adminAllowPath);
        when(request.getMethod()).thenReturn("GET");

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, new MockFilterChain());

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_UNAUTHORIZED);
    }

    @ParameterizedTest
    @MethodSource("notAllowHttpMethodParameterizedTestData")
    void path_에_대한_허용_http_method_권한이_없는_사용자가_요청하면_접근이_불가능해야_한다(User user, String path) throws ServletException, IOException {
        // given
        when(JwtTokenProvider.getUserIdFromJwt(any())).thenReturn(user.getLoginId());
        when(userRepository.findUsersByLoginId(any())).thenReturn(Optional.of(user));
        when(request.getRequestURI()).thenReturn(path);
        when(request.getMethod()).thenReturn("POST");

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, new MockFilterChain());

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_UNAUTHORIZED);
    }

    @ParameterizedTest
    @MethodSource("allTestTargetUserParameterizedTestData")
    void 유저의_타입에_의한_기본_권한의_path_로_접근하면_접근이_가능해야_한다(User user) throws ServletException, IOException {
        // given
        when(JwtTokenProvider.getUserIdFromJwt(any())).thenReturn(user.getLoginId());
        when(userRepository.findUsersByLoginId(any())).thenReturn(Optional.of(user));
        when(request.getRequestURI()).thenReturn(userTypeAllowPath);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, new MockFilterChain());

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_OK);
    }

    @ParameterizedTest
    @MethodSource("allowParameterizedTestData")
    void path_에_권한이_있는_사용자가_요청하면_접근이_가능해야_한다(User user, List<String> paths) throws ServletException, IOException {
        when(JwtTokenProvider.getUserIdFromJwt(any())).thenReturn(user.getLoginId());
        when(userRepository.findUsersByLoginId(any())).thenReturn(Optional.of(user));
        when(request.getMethod()).thenReturn("GET");

        for (String path : paths) {
            when(request.getRequestURI()).thenReturn(path);

            // when
            jwtAuthenticationFilter.doFilterInternal(request, response, new MockFilterChain());

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_OK);
        }
    }

    private static Stream<Arguments> notAllowParameterizedTestData() {
        return Stream.of(
                Arguments.of(seller),
                Arguments.of(normal)
        );
    }

    private static Stream<Arguments> notAllowHttpMethodParameterizedTestData() {
        return Stream.of(
                Arguments.of(seller, sellerAllowPath),
                Arguments.of(normal, normalAllowPath)
        );
    }

    private static Stream<Arguments> allTestTargetUserParameterizedTestData() {
        return Stream.of(
                Arguments.of(admin),
                Arguments.of(seller),
                Arguments.of(normal)
        );
    }

    private static Stream<Arguments> allowParameterizedTestData() {
        return Stream.of(
                Arguments.of(admin, Arrays.asList(adminAllowPath, sellerAllowPath, normalAllowPath, userTypeAllowPath, allAllowPath)),
                Arguments.of(seller, Arrays.asList(sellerAllowPath, normalAllowPath, userTypeAllowPath, allAllowPath)),
                Arguments.of(normal, Arrays.asList(normalAllowPath, userTypeAllowPath, allAllowPath))
        );
    }
}
