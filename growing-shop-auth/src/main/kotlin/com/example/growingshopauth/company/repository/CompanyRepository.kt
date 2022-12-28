package com.example.growingshopauth.company.repository

import com.example.growingshopauth.company.domain.Company
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CompanyRepository : JpaRepository<Company, Long> {
}
