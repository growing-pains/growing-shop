package com.example.growingshopauth.company.service;

import com.example.growingshopauth.company.domain.Company;
import com.example.growingshopauth.company.dto.CompanyResponse;
import com.example.growingshopauth.company.repository.CompanyRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepo companyRepo;

    public CompanyResponse.CompanyRes getCompany(Long id) {
        Company company = companyRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id + " 에 해당하는 업체가 존재하지 않습니다."));

        return CompanyResponse.CompanyRes.from(company);
    }
}
