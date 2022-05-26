package com.example.growingshop.domain.auth.dto;

import com.example.growingshop.domain.company.domain.CompanyId;
import com.example.growingshop.domain.user.domain.User;
import com.example.growingshop.domain.user.domain.UserGrade;
import com.example.growingshop.domain.user.domain.UserStatus;
import com.example.growingshop.domain.user.domain.UserType;
import com.example.growingshop.global.validator.StringContain;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class AuthRequest {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LoginReq {
        @StringContain(checkLowerEn = true, checkUpperEn = true, checkNumber = true, hasSpecialCharacter = "-_")
        @Column(nullable = false)
        private String loginId;

        @Column(nullable = false)
        private String password;
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class JoinReq {
        @NotBlank
        @Size(max = 30)
        @StringContain(checkLowerEn = true, checkUpperEn = true, checkKo = true, hasSpecialCharacter = "'-.")
        private String name;

        @Size(max = 13, min = 9)
        @NotBlank
        @Pattern(regexp = "^[0-9]{2,3}-?[0-9]{3,4}-?[0-9]{4}$")
        private String mobile;

        @Email
        @NotBlank
        private String email;

        @StringContain(checkLowerEn = true, checkUpperEn = true, checkNumber = true, hasSpecialCharacter = "-_")
        @Column(nullable = false)
        private String loginId;

        @Column(nullable = false)
        private String password;

        private CompanyId company;

        public String getJoinPassword() {
            return this.password;
        }

        public User toEntity(String hashedPassword) {
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
