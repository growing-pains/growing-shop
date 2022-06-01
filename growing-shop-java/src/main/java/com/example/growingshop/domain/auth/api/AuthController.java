package com.example.growingshop.domain.auth.api;

import com.example.growingshop.domain.auth.dto.AuthRequest;
import com.example.growingshop.domain.auth.dto.AuthResponse;
import com.example.growingshop.domain.user.domain.User;
import com.example.growingshop.domain.user.service.UserService;
import com.example.growingshop.global.config.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse.TokenRes> login(@RequestBody @Validated AuthRequest.LoginReq login) throws IllegalAccessException {
        return new ResponseEntity<>(jwtTokenProvider.generateToken(login), HttpStatus.OK);
    }

    @PostMapping("/join")
    public ResponseEntity<AuthResponse.JoinRes> join(@RequestBody @Validated AuthRequest.JoinReq join) {
        User joinResult = userService.joinUser(join);

        return new ResponseEntity<>(
                AuthResponse.JoinRes.builder().state(joinResult.isPersist()).build(), HttpStatus.CREATED
        );
    }
}
