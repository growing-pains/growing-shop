package com.example.growingshop.global.config.security;

import com.example.growingshop.domain.auth.domain.Privilege;
import com.example.growingshop.domain.auth.domain.Privileges;
import com.example.growingshop.domain.auth.domain.Role;
import com.example.growingshop.domain.auth.domain.Roles;
import com.example.growingshop.domain.auth.service.PrivilegeService;
import com.example.growingshop.domain.user.domain.User;
import com.example.growingshop.domain.user.domain.UserType;
import com.example.growingshop.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccessiblePathTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PrivilegeService privilegeService;

    @InjectMocks
    private AccessiblePath accessiblePath;

    @Spy
    private HttpServletRequest request;

    @Spy
    private Authentication authentication;

    private Privilege adminPrivilege = new Privilege("admin", "/admin", "");
    private Privilege sellerPrivilege = new Privilege("seller", "/seller", "");
    private Privilege normalPrivilege = new Privilege("normal", "/normal", "");

    private Roles adminRole = new Roles(Collections.singletonList(
            new Role("ADMIN", Arrays.asList(adminPrivilege, sellerPrivilege, normalPrivilege))
    ));
    private Roles sellerRole = new Roles(Collections.singletonList(
            new Role("SELLER", Arrays.asList(sellerPrivilege, normalPrivilege))
    ));
    private Roles normalRole = new Roles(Collections.singletonList(
            new Role("NORMAL", Collections.singletonList(normalPrivilege))
    ));

    private User admin = User.builder().type(UserType.ADMIN).roles(adminRole).build();
    private User seller = User.builder().type(UserType.NORMAL).roles(sellerRole).build();
    private User normal = User.builder().type(UserType.SELLER).roles(normalRole).build();

    @BeforeEach
    void beforeEach()
    {
        MockitoAnnotations.openMocks(this);

        when(authentication.getPrincipal()).thenReturn("");
        when(privilegeService.findAll()).thenReturn(new Privileges(Arrays.asList(adminPrivilege, sellerPrivilege, normalPrivilege)));
    }

    @Test
    void role_에_없는_경로_요청시_권한과_상관없이_접근이_가능해야_한다() {
        // given
        when(request.getPathInfo()).thenReturn("/all");

        // when
        boolean result = accessiblePath.check(request, authentication);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void path_에_대한_권한이_없는_사용자가_요청하면_접근이_불가능해야_한다() {
        // given
        when(request.getPathInfo()).thenReturn("/admin");
        when(authentication.getPrincipal()).thenReturn("");
        when(userRepository.findUsersByLoginId(any())).thenReturn(Optional.of(seller));

        // when
        boolean result = accessiblePath.check(request, authentication);

        // then
        assertThat(result).isFalse();
    }
}
