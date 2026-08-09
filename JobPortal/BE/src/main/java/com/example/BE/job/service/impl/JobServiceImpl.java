package com.example.BE.job.service.impl;


import com.example.BE.dto.JobApplicationDto;
import com.example.BE.dto.JobDto;
import com.example.BE.dto.ProfileDto;
import com.example.BE.dto.UpdateJobApplicationDto;
import com.example.BE.entity.Job;
import com.example.BE.entity.JobApplication;
import com.example.BE.entity.JobPortalUser;
import com.example.BE.entity.Profile;
import com.example.BE.job.mapper.JobMapper;
import com.example.BE.job.service.IJobService;
import com.example.BE.repository.JobApplicationRepository;
import com.example.BE.repository.JobPortalUserRepository;
import com.example.BE.repository.JobRepository;
import com.example.BE.security.util.ApplicationUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobServiceImpl implements IJobService {

    private final JobMapper jobMapper;
    private final JobApplicationRepository jobApplicationRepository;
    private final JobRepository jobRepository;
    private final JobPortalUserRepository userRepository;


    @Override
    public List<JobDto> getEmployerJobs(String employerEmail) {
        JobPortalUser employer = userRepository.findJobPortalUserByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        if (employer.getCompany() == null) {
            throw new RuntimeException("Employer does not have a company assigned");
        }

        List<Job> jobs = employer.getCompany().getJobs();
        return jobs.stream()
                .map(job -> jobMapper.transformJobToDto(job))
                .collect(Collectors.toList());
    }



    @Transactional
    @Override
    public JobDto updateJobStatus(Long jobId, String status, String employerEmail) {
        // Validate status
        if (!status.equals("ACTIVE") && !status.equals("CLOSED") && !status.equals("DRAFT")) {
            throw new RuntimeException("Invalid status. Must be ACTIVE, CLOSED, or DRAFT");
        }
        JobPortalUser employer = userRepository.findJobPortalUserByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        if (employer.getCompany() == null) {
            throw new RuntimeException("Employer does not have a company assigned");
        }
        Job job = employer.getCompany().getJobs().stream().filter(j -> j.getId().equals(jobId)).findFirst()
                .orElseThrow(() -> new RuntimeException("Job not found"));
        job.setStatus(status);
        return jobMapper.transformJobToDto(job);
    }



    @Override
    @Transactional
    public JobDto createJob(JobDto jobDto, String employerEmail) {
        // Validate employer and get their company
        JobPortalUser employer = userRepository.findJobPortalUserByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("Employer not found"));
        if (employer.getCompany() == null) {
            throw new RuntimeException("Employer does not have a company assigned. Please contact admin.");
        }
        Job job = jobMapper.tranformDtoToEntity(jobDto);
        job.setPostedDate(Instant.now());
        job.setApplicationsCount(0);
        job.setStatus("DRAFT");
        job.setCompany(employer.getCompany());
        Job savedJob = jobRepository.save(job);
        return jobMapper.transformJobToDto(savedJob);
    }


    @Override
    public List<JobApplicationDto> getApplicationsByJobForEmployer(Long jobId) {
        List<JobApplication> applications = jobApplicationRepository.findByJobIdOrderByAppliedAtAsc(jobId);
        return applications.stream()
                .map(jobApplication -> jobMapper.mapToJobApplicationDto(jobApplication))
                .collect(Collectors.toList());
    }


    @Transactional
    @Override
    public boolean updateJobApplication(UpdateJobApplicationDto dto) {
        int updatedRows = jobApplicationRepository.updateStatusAndNotesById(
                dto.status().name(), dto.notes(),dto.applicationId(), ApplicationUtility.getLoggedInUser());
        return updatedRows > 0;
    }


}
