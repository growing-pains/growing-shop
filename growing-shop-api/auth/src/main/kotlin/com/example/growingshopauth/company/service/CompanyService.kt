package com.example.growingshopauth.company.service

import com.example.growingshopauth.company.dto.CompanyResponse
import com.example.repository.company.CompanyRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CompanyService(
    private val companyRepository: CompanyRepository
) {

    fun getCompany(id: Long): CompanyResponse.CompanyRes = CompanyResponse.CompanyRes.from(
        companyRepository.findById(id)
            .orElseThrow { EntityNotFoundException("$id 에 해당하는 업체가 존재하지 않습니다.") }
    )
}
