package com.example.BE.company.service.impl;

import com.example.BE.constants.ApplicationConstants;
import com.example.BE.dto.CompanyDto;
import com.example.BE.dto.JobDto;
import com.example.BE.entity.Company;
import com.example.BE.entity.Job;
import com.example.BE.repository.CompanyRepository;
import com.example.BE.company.service.ICompanyService;
import lombok.RequiredArgsConstructor;
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
        List<Company> companyList =companyRepository.findAllWithJobsByStatus(ApplicationConstants.ACTIVE_STATUS);
        return companyList.stream().map(
                company -> transformToDto(company)
        ).collect(Collectors.toList());
    }


    private CompanyDto transformToDto(Company company) {
        List<JobDto> listJob = company.getJobs().stream().map(
                job -> tramsFormtJobToDto(job)
        ).collect(Collectors.toList());
        return new CompanyDto(company.getId(), company.getName(), company.getLogo(),
                company.getIndustry(), company.getSize(), company.getRating(),
                company.getLocations(), company.getFounded(), company.getDescription(),
                company.getEmployees(), company.getWebsite(), company.getCreatedAt(),listJob);
    }
    private JobDto tramsFormtJobToDto(Job job){
        return new JobDto(
                job.getId(),
                job.getTitle(),
                job.getCompany().getId(),
                job.getCompany().getName(),
                job.getCompany().getLogo(),
                job.getLocation(),
                job.getWorkType(),
                job.getJobType(),
                job.getCategory(),
                job.getExperienceLevel(),
                job.getSalaryMin(),
                job.getSalaryMax(),
                job.getSalaryCurrency(),
                job.getSalaryPeriod(),
                job.getDescription(),
                job.getRequirements(),
                job.getBenefits(),
                job.getPostedDate(),
                job.getApplicationDeadline(),
                job.getApplicationsCount(),
                job.getFeatured(),
                job.getUrgent(),
                job.getRemote(),
                job.getStatus()
        );
    }
}
