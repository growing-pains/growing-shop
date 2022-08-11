package com.example.growingshop.domain.auth.service;

import com.example.growingshop.domain.auth.dto.AuthRequest;
import com.example.growingshop.domain.user.domain.User;
import com.example.growingshop.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public boolean matchLoginUser(AuthRequest.LoginReq login) {
        Optional<User> user = userRepository.findUsersByLoginId(login.getLoginId());

        return user.filter(value -> passwordEncoder.matches(login.getPassword(), value.getPassword()))
                .isPresent();
    }
}
