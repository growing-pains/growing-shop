package com.example.growingshop.domain.company.dto;

import com.example.growingshop.domain.company.domain.Company;
import com.example.growingshop.domain.company.domain.CompanyGrade;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompanyResponse {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    @Builder
    @Getter
    public static class CompanyRes {
        private Long id;
        private String name;
        private String businessRegistrationNumber;
        private CompanyGrade grade;

        public static CompanyRes from(Company company) {
            CompanyRes res = new CompanyRes();

            res.id = company.getId();
            res.name = company.getName();
            res.businessRegistrationNumber = company.getBusinessRegistrationNumber();
            res.grade = company.getGrade();

            return res;
        }
    }
}
