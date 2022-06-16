package com.example.growingshop.domain.company.service;

import com.example.growingshop.domain.company.domain.Company;
import com.example.growingshop.domain.company.repository.CompanyRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepo companyRepo;

    public Company getCompany(Long id) {
        return companyRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id + " 에 해당하는 업체가 존재하지 않습니다."));
    }
}
