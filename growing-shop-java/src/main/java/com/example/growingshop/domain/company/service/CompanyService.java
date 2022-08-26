package com.example.growingshop.domain.company.service;

import com.example.growingshop.domain.company.domain.Company;
import com.example.growingshop.domain.company.dto.CompanyResponse;
import com.example.growingshop.domain.company.repository.CompanyRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

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
