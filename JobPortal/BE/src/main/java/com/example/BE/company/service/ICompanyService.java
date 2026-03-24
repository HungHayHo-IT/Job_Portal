package com.example.BE.service;

import com.example.BE.dto.CompanyDto;
import com.example.BE.entity.Company;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ICompanyService {

    List<CompanyDto> getAllCompanies();
}
