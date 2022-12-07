package com.example.growingshop.domain.user.dto;

import com.example.growingshop.domain.company.dto.CompanyResponse;
import com.example.growingshop.domain.user.domain.User;
import com.example.growingshop.domain.user.domain.UserGrade;
import com.example.growingshop.domain.user.domain.UserType;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponse {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    @Builder
    @Getter
    public static class UserRes {
        private Long id;
        private String name;
        private String mobile;
        private String email;
        private String loginId;
        private CompanyResponse.CompanyRes company;
        private UserType type;
        private UserGrade grade;

        public static UserRes from(User user) {
            UserRes res = new UserRes();

            res.id = user.getId();
            res.name = user.getName();
            res.mobile = user.getMobile();
            res.email = user.getEmail();
            res.loginId = user.getLoginId();

            if (user.getCompany() != null) {
                res.company = CompanyResponse.CompanyRes.from(user.getCompany());
            }

            res.type = user.getType();
            res.grade = user.getGrade();

            return res;
        }
    }
}
