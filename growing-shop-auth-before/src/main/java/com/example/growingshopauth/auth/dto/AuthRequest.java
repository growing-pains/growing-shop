package com.example.growingshopauth.auth.dto;

import com.example.growingshopauth.company.domain.Company;
import com.example.growingshopauth.user.domain.User;
import com.example.growingshopauth.user.domain.UserGrade;
import com.example.growingshopauth.user.domain.UserStatus;
import com.example.growingshopauth.user.domain.UserType;
import com.example.growingshopcommon.validator.StringChecker;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthRequest {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class LoginReq {
        @StringChecker(includeLowerEn = true, includeUpperEn = true, includeNumber = true, includeSpecialCharacter = "-_")
        @Column(nullable = false)
        private String loginId;

        @Column(nullable = false)
        private String password;
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    @Builder
    public static class JoinReq {
        @NotBlank
        @Size(max = 30)
        @StringChecker(includeLowerEn = true, includeUpperEn = true, includeKo = true, includeSpecialCharacter = "'-.")
        private String name;

        @Size(max = 13, min = 9)
        @NotBlank
        @Pattern(regexp = "^[0-9]{2,3}-?[0-9]{3,4}-?[0-9]{4}$")
        private String mobile;

        @Email
        @NotBlank
        private String email;

        @StringChecker(includeLowerEn = true, includeUpperEn = true, includeNumber = true, includeSpecialCharacter = "-_")
        @Column(nullable = false)
        private String loginId;

        @Column(nullable = false)
        private String password;

        private Long company;

        public User toEntity(String hashedPassword, Company company) {
            return User.builder()
                    .name(this.name)
                    .mobile(this.mobile)
                    .email(this.email)
                    .loginId(this.loginId)
                    .password(hashedPassword)
                    .company(company)
                    .status(company != null ? UserStatus.UNDER_REVIEW : UserStatus.NORMAL)
                    .type(company != null ? UserType.SELLER : UserType.NORMAL)
                    .grade(UserGrade.NORMAL)
                    .build();
        }
    }
}
