package com.example.BE.service.impl;

import com.example.BE.dto.CompanyDto;
import com.example.BE.entity.Company;
import com.example.BE.repository.CompanyRepository;
import com.example.BE.service.ICompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ICompanyServiceImpl implements ICompanyService
{
    private final CompanyRepository companyRepository;



    @Override
    public List<CompanyDto> getAllCompanies() {
        List<Company> companyList =companyRepository.findAll();
        return companyList.stream().map(
                company -> transformToDto(company)
        ).collect(Collectors.toList());
    }


    private CompanyDto transformToDto(Company company) {
        return new CompanyDto(company.getId(), company.getName(), company.getLogo(),
                company.getIndustry(), company.getSize(), company.getRating(),
                company.getLocations(), company.getFounded(), company.getDescription(),
                company.getEmployees(), company.getWebsite(), company.getCreatedAt());
    }
}
