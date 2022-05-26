package com.example.growingshop.domain.user.service;

import com.example.growingshop.domain.auth.dto.AuthRequest;
import com.example.growingshop.domain.user.domain.User;
import com.example.growingshop.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User joinUser(AuthRequest.JoinReq join) {
        return userRepository.save(
                join.toEntity(passwordEncoder.encode(join.getJoinPassword()))
        );
    }
}
