package com.example.growingshop.global.config.security;

import com.example.growingshop.domain.auth.domain.Privilege;
import com.example.growingshop.domain.auth.domain.Privileges;
import com.example.growingshop.domain.auth.domain.Role;
import com.example.growingshop.domain.auth.domain.Roles;
import com.example.growingshop.domain.auth.service.PrivilegeService;
import com.example.growingshop.domain.auth.service.RoleService;
import com.example.growingshop.domain.user.domain.User;
import com.example.growingshop.domain.user.domain.UserType;
import com.example.growingshop.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccessiblePathTest {

    private UserRepository userRepository = mock(UserRepository.class);
    private PrivilegeService privilegeService = mock(PrivilegeService.class);
    private RoleService roleService = mock(RoleService.class);

    private HttpServletRequest request = spy(HttpServletRequest.class);
    private Authentication authentication = spy(Authentication.class);

    @InjectMocks
    private AccessiblePath accessiblePath;

    private static final String adminAllowPath = "/admin";
    private static final String sellerAllowPath = "/seller";
    private static final String normalAllowPath = "/normal";
    private static final String userTypeAllowPath = "/default";
    private static final String allAllowPath = "/all";

    private static final Privilege adminPrivilege = new Privilege("admin", adminAllowPath, "");
    private static final Privilege sellerPrivilege = new Privilege("seller", sellerAllowPath, "");
    private static final Privilege normalPrivilege = new Privilege("normal", normalAllowPath, "");
    private static final Privilege userTypePrivilege = new Privilege("default", userTypeAllowPath, "");

    private static final Roles adminRole = new Roles(Collections.singletonList(
            new Role("ADMIN", Arrays.asList(adminPrivilege, sellerPrivilege, normalPrivilege))
    ));
    private static final Roles sellerRole = new Roles(Collections.singletonList(
            new Role("SELLER", Arrays.asList(sellerPrivilege, normalPrivilege))
    ));
    private static final Roles normalRole = new Roles(Collections.singletonList(
            new Role("NORMAL", Collections.singletonList(normalPrivilege))
    ));
    private static final Role userTypeRole = new Role("DEFAULT", Collections.singletonList(userTypePrivilege));

    private static final User admin = User.builder().type(UserType.ADMIN).roles(adminRole).build();
    private static final User seller = User.builder().type(UserType.NORMAL).roles(sellerRole).build();
    private static final User normal = User.builder().type(UserType.SELLER).roles(normalRole).build();

    @BeforeEach
    void beforeEach()
    {
        MockitoAnnotations.openMocks(this);

        when(authentication.getPrincipal()).thenReturn("");
        when(privilegeService.findAll()).thenReturn(new Privileges(Arrays.asList(adminPrivilege, sellerPrivilege, normalPrivilege)));
        when(roleService.findByName(any())).thenReturn(userTypeRole);
    }

    @Test
    void role_에_없는_경로_요청시_권한과_상관없이_접근이_가능해야_한다() {
        // given
        when(request.getPathInfo()).thenReturn(allAllowPath);

        // when
        boolean result = accessiblePath.check(request, authentication);

        // then
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @MethodSource("notAllowPathParameterizedTestData")
    void path_에_대한_권한이_없는_사용자가_요청하면_접근이_불가능해야_한다(User user) {
        // given
        when(request.getPathInfo()).thenReturn(adminAllowPath);
        when(userRepository.findUsersByLoginId(any())).thenReturn(Optional.of(user));

        // when
        boolean resultBySeller = accessiblePath.check(request, authentication);

        // then
        assertThat(resultBySeller).isFalse();
    }

    @ParameterizedTest
    @MethodSource("allTestTargetUserParameterizedTestData")
    void 유저의_타입에_의한_기본_권한의_path_로_접근하면_접근이_가능해야_한다(User user) {
        // given
        when(request.getPathInfo()).thenReturn(userTypeAllowPath);
        when(userRepository.findUsersByLoginId(any())).thenReturn(Optional.of(user));

        // when
        boolean result = accessiblePath.check(request, authentication);

        // then
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @MethodSource("allowPathParameterizedTestData")
    void path_에_권한이_있는_사용자가_요청하면_접근이_가능해야_한다(User user, List<String> paths) {
        when(userRepository.findUsersByLoginId(any())).thenReturn(Optional.of(user));
        paths.forEach(path -> {
            when(request.getPathInfo()).thenReturn(path);

            // when
            boolean result = accessiblePath.check(request, authentication);

            // then
            assertThat(result).isTrue();
        });
    }

    private static Stream<Arguments> notAllowPathParameterizedTestData() {
        return Stream.of(
                Arguments.of(seller),
                Arguments.of(normal)
        );
    }

    private static Stream<Arguments> allTestTargetUserParameterizedTestData() {
        return Stream.of(
                Arguments.of(admin),
                Arguments.of(seller),
                Arguments.of(normal)
        );
    }

    private static Stream<Arguments> allowPathParameterizedTestData() {
        return Stream.of(
                Arguments.of(admin, Arrays.asList(adminAllowPath, sellerAllowPath, normalAllowPath, userTypeAllowPath, allAllowPath)),
                Arguments.of(seller, Arrays.asList(sellerAllowPath, normalAllowPath, userTypeAllowPath, allAllowPath)),
                Arguments.of(normal, Arrays.asList(normalAllowPath, userTypeAllowPath, allAllowPath))
        );
    }
}
