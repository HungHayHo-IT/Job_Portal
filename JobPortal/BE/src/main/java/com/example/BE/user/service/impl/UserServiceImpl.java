package com.example.BE.user.service.impl;

import com.example.BE.constants.ApplicationConstants;
import com.example.BE.dto.UserDto;
import com.example.BE.entity.Company;
import com.example.BE.entity.JobPortalUser;
import com.example.BE.entity.Role;
import com.example.BE.repository.CompanyRepository;
import com.example.BE.repository.JobPortalUserRepository;
import com.example.BE.repository.RoleRepository;
import com.example.BE.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements IUserService {

    private final JobPortalUserRepository jobPortalUserRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;

    @Override
    public Optional<UserDto> searchUserByEmail(String email) {
        return jobPortalUserRepository.findJobPortalUserByEmail(email).map(
                user -> mapToUserDto(user)
        );
    }

    @Transactional
    @Override
    public UserDto elevateToEmployer(Long userId) {
        JobPortalUser jobPortalUser = jobPortalUserRepository.findById(userId).orElseThrow(
                ()->new RuntimeException("User not found with ID : " +userId)
        );

        if(ApplicationConstants.ROLE_EMPLOYER.equals(jobPortalUser.getRole().getName())){
            return mapToUserDto(jobPortalUser);
        }

        if(ApplicationConstants.ROLE_ADMIN.equals(jobPortalUser.getRole().getName())){
            throw new RuntimeException("Cannot elevate admin user to employer role");
        }

        Role roleEmployer = roleRepository.findRoleByName(ApplicationConstants.ROLE_EMPLOYER).orElseThrow(
                () -> new RuntimeException("ROLE_EMPLOYER not found")
        );

        jobPortalUser.setRole(roleEmployer);

        return mapToUserDto(jobPortalUser);
    }

    @Override
    public UserDto assignCompanyToEmployer(Long userId, Long companyId) {
        JobPortalUser user = jobPortalUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));


        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));
        user.setCompany(company);
        return mapToUserDto(user);
    }

    private UserDto mapToUserDto(JobPortalUser user) {
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(user, dto);
        dto.setUserId(user.getId());
        dto.setRole(user.getRole() != null ? user.getRole().getName() : null);
        dto.setCompanyId(user.getCompany() != null ? user.getCompany().getId() : null);
        dto.setCompanyName(user.getCompany() != null ? user.getCompany().getName() : null);
        return dto;
    }
}
