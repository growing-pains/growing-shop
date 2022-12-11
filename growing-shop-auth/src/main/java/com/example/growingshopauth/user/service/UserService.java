package com.example.growingshopauth.user.service;

import com.example.growingshopauth.auth.dto.AuthRequest;
import com.example.growingshopauth.company.domain.Company;
import com.example.growingshopauth.company.repository.CompanyRepo;
import com.example.growingshopauth.user.dto.UserResponse;
import com.example.growingshopauth.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyRepo companyRepo;

    @Transactional
    public UserResponse.UserRes joinUser(AuthRequest.JoinReq join) {
        if (userRepository.findUsersByLoginId(join.getLoginId()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 아이디입니다.");
        }

        Company company = null;

        if (join.getCompany() != null) {
            company = companyRepo.findById(join.getCompany())
                    .orElseThrow(() -> new IllegalArgumentException(join.getCompany() + " 에 해당하는 업체가 존재하지 않습니다."));
        }

        return UserResponse.UserRes.from(
                userRepository.save(
                        join.toEntity(passwordEncoder.encode(join.getPassword()), company)
                )
        );
    }
}
