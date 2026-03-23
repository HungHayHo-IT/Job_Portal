package com.example.BE.service.impl;

import com.example.BE.entity.Company;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICompanyService {

    List<Company> getAllCompanies();
}
