package com.example.BE.user.service.impl;

import com.example.BE.constants.ApplicationConstants;
import com.example.BE.dto.*;
import com.example.BE.entity.*;
import com.example.BE.repository.*;
import com.example.BE.user.mapper.UserMapper;
import com.example.BE.user.service.IUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements IUserService {

    private final UserMapper userMapper;
    private final JobPortalUserRepository jobPortalUserRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final ProfileRepository profileRepository;
    private final JobRepository jobRepository;
    private final JobApplicationRepository jobApplicationRepository;
    @Override
    public Optional<UserDto> searchUserByEmail(String email) {
        return jobPortalUserRepository.findJobPortalUserByEmail(email).map(
                user -> userMapper.mapToUserDto(user)
        );
    }

    @Transactional
    @Override
    public UserDto elevateToEmployer(Long userId) {
        JobPortalUser jobPortalUser = jobPortalUserRepository.findById(userId).orElseThrow(
                ()->new RuntimeException("User not found with ID : " +userId)
        );

        if(ApplicationConstants.ROLE_EMPLOYER.equals(jobPortalUser.getRole().getName())){
            return userMapper.mapToUserDto(jobPortalUser);
        }

        if(ApplicationConstants.ROLE_ADMIN.equals(jobPortalUser.getRole().getName())){
            throw new RuntimeException("Cannot elevate admin user to employer role");
        }

        Role roleEmployer = roleRepository.findRoleByName(ApplicationConstants.ROLE_EMPLOYER).orElseThrow(
                () -> new RuntimeException("ROLE_EMPLOYER not found")
        );

        jobPortalUser.setRole(roleEmployer);

        return userMapper.mapToUserDto(jobPortalUser);
    }

    @Transactional
    @Override
    public UserDto assignCompanyToEmployer(Long userId, Long companyId) {
        JobPortalUser user = jobPortalUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));


        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));
        user.setCompany(company);
        return userMapper.mapToUserDto(user);
    }

    @Transactional
    @Override
    public ProfileDto createOrUpdateProfile(String userEmail, String profileJson,
                                            MultipartFile profilePicture, MultipartFile resume) throws JsonProcessingException {
        JobPortalUser user = jobPortalUserRepository.findJobPortalUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));
        Profile profile = user.getProfile();
        if (null == profile) {
            profile = new Profile();
            profile.setUser(user);
        }
        ObjectMapper objectMapper = new ObjectMapper();//ObjectMapper của Jackson dùng để chuyển chuỗi JSON thành object ProfileDto.
        // Parse JSON string to ProfileDto
        ProfileDto profileDto = objectMapper.readValue(profileJson, ProfileDto.class);
        Profile savedProfile = profileRepository.save(userMapper.mapToProfile(profile, profileDto, profilePicture, resume));
        return userMapper.mapToProfileDto(savedProfile, false);
    }

    @Override
    public ProfileDto getProfile(String userEmail) {
        JobPortalUser user = jobPortalUserRepository.findJobPortalUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));
        if (user.getProfile() == null) {
            return null;
        }
        return userMapper.mapToProfileDto(user.getProfile(), false);
    }

    @Override
    public ProfileDto getProfilePicture(String userEmail) {
        JobPortalUser user = jobPortalUserRepository.findJobPortalUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));
        if (user.getProfile() == null) {
            return null;
        }
        return userMapper.mapToProfileDto(user.getProfile(), true);
    }

    @Override
    public ProfileDto getResume(String userEmail) {
        JobPortalUser user = jobPortalUserRepository.findJobPortalUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));
        if (user.getProfile() == null) {
            return null;
        }
        return userMapper.mapToProfileDto(user.getProfile(), true);
    }

    @Transactional
    @Override
    public JobDto saveJob(String userEmail, Long jobId) {
        JobPortalUser user = jobPortalUserRepository.findJobPortalUserByEmail(userEmail).orElseThrow(
                ()->new RuntimeException("User not found with email: " + userEmail)
        );
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId));
        user.getSavedJobs().add(job);
        return userMapper.transformJobToDto(job);
    }

    @Transactional
    @Override
    public void unsaveJob(String userEmail, Long jobId) {
        // Validate if user exists
        JobPortalUser user = jobPortalUserRepository.findJobPortalUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));
        // Validate job exists
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId));
        user.getSavedJobs().remove(job);
    }

    @Override
    public List<JobDto> getSavedJobs(String userEmail) {
        // Validate if user exists
        JobPortalUser user = jobPortalUserRepository.findJobPortalUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));
        return user.getSavedJobs().stream().map(job -> userMapper.transformJobToDto(job))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public JobApplicationDto applyForJob(String userEmail, ApplyJobRequestDto applyJobRequestDto) {
        // Validate if user exists
        JobPortalUser user = jobPortalUserRepository.findJobPortalUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));
        Long jobId = applyJobRequestDto.jobId();
        if (jobApplicationRepository.existsByUserIdAndJobId(user.getId(), jobId)) {
            throw new RuntimeException("You have already applied for this job");
        }
        // Validate job exists
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId));
        // Create job application
        JobApplication application = new JobApplication();
        application.setUser(user);
        application.setJob(job);
        application.setAppliedAt(Instant.now());
        application.setStatus(ApplicationConstants.PENDING);
        application.setCoverLetter(applyJobRequestDto.coverLetter());
        JobApplication saved = jobApplicationRepository.save(application);
        // Increment applications count
        job.setApplicationsCount(job.getApplicationsCount() != null ? job.getApplicationsCount() + 1 : 1);
        // jobRepository.save(job); - Optional
        return userMapper.mapToJobApplicationDto(saved);
    }

    @Transactional
    @Override
    public void withdrawApplication(String userEmail, Long jobId) {
        // Validate if user exists
        JobPortalUser user = jobPortalUserRepository.findJobPortalUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));
        if (!jobApplicationRepository.existsByUserIdAndJobId(user.getId(), jobId)) {
            throw new RuntimeException("You have not applied for this job");
        }
        jobApplicationRepository.deleteByUserIdAndJobId(user.getId(), jobId);
        // Get the job to update the count
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId));

        // Decrement applications count (ensure it doesn't go below 0)
        if (job.getApplicationsCount() != null && job.getApplicationsCount() > 0) {
            job.setApplicationsCount(job.getApplicationsCount() - 1);
            // jobRepository.save(job); - Optional
        }
    }

    @Override
    public List<JobApplicationDto> getJobSeekerApplications(String userEmail) {
        // Validate if user exists
        JobPortalUser user = jobPortalUserRepository.findJobPortalUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));
        return user.getJobApplications().stream().map(
                application -> userMapper.mapToJobApplicationDto(application)
                )
                .collect(Collectors.toList());
    }




}
