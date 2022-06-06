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
        if (userRepository.findUsersByLoginId(join.getLoginId()).isPresent()) {
//            throw new IllegalArgumentException("이미 가입된 회원입니다.");
            return User.builder().build();
        }

        return userRepository.save(
                join.toEntity(passwordEncoder.encode(join.getJoinPassword()))
        );
    }
}
