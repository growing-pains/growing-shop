package com.example.growingshopauth.company.dto

import com.example.growingshopauth.company.domain.Company
import com.example.growingshopauth.company.domain.CompanyGrade

class CompanyResponse {

    class CompanyRes(
        val id: Long,
        val name: String,
        val businessRegistrationNumber: String,
        val grade: CompanyGrade
    ) {
        companion object {
            fun from(company: Company): CompanyRes {
                return CompanyRes(
                    company.id,
                    company.name,
                    company.businessRegistrationNumber,
                    company.grade
                )
            }
        }
    }
}
