package com.example.BE.company.service.impl;

import com.example.BE.company.mapper.CompanyMapper;
import com.example.BE.constants.ApplicationConstants;
import com.example.BE.dto.CompanyDto;
import com.example.BE.dto.JobDto;
import com.example.BE.entity.Company;
import com.example.BE.entity.Job;
import com.example.BE.repository.CompanyRepository;
import com.example.BE.company.service.ICompanyService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ICompanyServiceImpl implements ICompanyService
{
    private final CompanyMapper companyMapper;
    private final CompanyRepository companyRepository;




    @Override
    @Cacheable(cacheNames = "public-companies")
    public List<CompanyDto> getAllCompanies() {
        List<Company> companyList =companyRepository.findAllWithJobsByStatus(ApplicationConstants.ACTIVE_STATUS);
        return companyList.stream().map(
                company -> companyMapper.transformCompanyToDto(company)
        ).collect(Collectors.toList());
    }


    @Cacheable("companies")
    @Override
    public List<CompanyDto> getAllCompaniesForAdmin() {
        List<Company> companyList = companyRepository.findAll();
        return companyList.stream().map(
                company -> companyMapper.transformCompanyToDtoForAdmin(company)
        ).collect(Collectors.toList());
    }


    @Transactional
    @Override
    public boolean createCompany(CompanyDto companyDto) {
        Company company = companyMapper.transformCompanyDtoToEntity(companyDto);
        Company savedCompany = companyRepository.save(company);
        return savedCompany.getId() != null && savedCompany.getId() > 0;
    }

    @Transactional
    @Override
    public boolean updateCompanyDetails(Long id, CompanyDto companyDto) {
        int updateCompany = companyRepository.updateCompanyDetails(
                id,companyDto.name(),companyDto.logo(),
                companyDto.industry(),companyDto.size(),companyDto.rating(),
                companyDto.locations(),companyDto.founded(),companyDto.description(),
                companyDto.employees(),companyDto.website()
        );
        return updateCompany>0;
    }

    @Transactional
    @Override
    public void deleteCompanyById(Long id) {
        companyRepository.deleteById(id);
    }
}
