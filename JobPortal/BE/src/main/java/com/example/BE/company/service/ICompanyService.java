package com.example.BE.company.service;

import com.example.BE.dto.CompanyDto;
import com.example.BE.entity.Company;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ICompanyService {

    List<CompanyDto> getAllCompanies();

    List<CompanyDto> getAllCompaniesForAdmin();

    boolean createCompany(CompanyDto companyDto);

    boolean updateCompanyDetails(Long id , CompanyDto companyDto);

    void deleteCompanyById(Long id);

}
