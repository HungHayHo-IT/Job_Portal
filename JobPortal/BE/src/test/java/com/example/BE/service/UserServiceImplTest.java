package com.example.BE.service;
import com.example.BE.constants.ApplicationConstants;
import com.example.BE.dto.*;
import com.example.BE.entity.*;
import com.example.BE.repository.*;
import com.example.BE.user.mapper.UserMapper;
import com.example.BE.user.service.impl.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private JobPortalUserRepository jobPortalUserRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private JobPortalUser user;
    private Role role;
    private Company company;
    private Job job;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setName("ROLE_JOB_SEEKER");

        company = new Company();
        company.setId(1L);
        company.setName("OpenAI");

        job = new Job();
        job.setId(1L);
        job.setApplicationsCount(0);

        user = new JobPortalUser();
        user.setId(1L);
        user.setEmail("test@gmail.com");
        user.setRole(role);
        user.setSavedJobs(new HashSet<>());
        user.setJobApplications(new HashSet<>());
    }

    @Test
    void testSearchUserByEmail() {
        // Arrange
        UserDto dto = mock(UserDto.class);

        when(jobPortalUserRepository.findJobPortalUserByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));
        when(userMapper.mapToUserDto(user)).thenReturn(dto);

        // Act
        Optional<UserDto> result = userService.searchUserByEmail("test@gmail.com");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
    }

    @Test
    void testElevateToEmployer() {
        // Arrange
        Role employerRole = new Role();
        employerRole.setName(ApplicationConstants.ROLE_EMPLOYER);

        UserDto dto = mock(UserDto.class);

        when(jobPortalUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findRoleByName(ApplicationConstants.ROLE_EMPLOYER))
                .thenReturn(Optional.of(employerRole));
        when(userMapper.mapToUserDto(user)).thenReturn(dto);

        // Act
        UserDto result = userService.elevateToEmployer(1L);

        // Assert
        assertEquals(dto, result);
        assertEquals(employerRole, user.getRole());
    }

    @Test
    void testAssignCompanyToEmployer() {
        // Arrange
        UserDto dto = mock(UserDto.class);

        when(jobPortalUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(userMapper.mapToUserDto(user)).thenReturn(dto);

        // Act
        UserDto result = userService.assignCompanyToEmployer(1L, 1L);

        // Assert
        assertEquals(dto, result);
        assertEquals(company, user.getCompany());
    }

    @Test
    void testCreateOrUpdateProfile() throws JsonProcessingException {
        // Arrange
        String profileJson = """
                {
                  "jobTitle":"Java Developer",
                  "location":"Hue"
                }
                """;

        Profile profile = new Profile();
        user.setProfile(profile);

        ProfileDto dto = mock(ProfileDto.class);

        MultipartFile picture =
                new MockMultipartFile("file", "pic.jpg", "image/jpeg", "abc".getBytes());

        MultipartFile resume =
                new MockMultipartFile("file", "cv.pdf", "application/pdf", "xyz".getBytes());

        when(jobPortalUserRepository.findJobPortalUserByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(userMapper.mapToProfile(any(), any(), any(), any()))
                .thenReturn(profile);

        when(profileRepository.save(any())).thenReturn(profile);

        when(userMapper.mapToProfileDto(profile, false)).thenReturn(dto);

        // Act
        ProfileDto result = userService.createOrUpdateProfile(
                user.getEmail(),
                profileJson,
                picture,
                resume
        );

        // Assert
        assertEquals(dto, result);
    }

    @Test
    void testGetProfile() {
        // Arrange
        Profile profile = new Profile();
        user.setProfile(profile);

        ProfileDto dto = mock(ProfileDto.class);

        when(jobPortalUserRepository.findJobPortalUserByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(userMapper.mapToProfileDto(profile, false)).thenReturn(dto);

        // Act
        ProfileDto result = userService.getProfile(user.getEmail());

        // Assert
        assertEquals(dto, result);
    }

    @Test
    void testGetProfilePicture() {
        // Arrange
        Profile profile = new Profile();
        user.setProfile(profile);

        ProfileDto dto = mock(ProfileDto.class);

        when(jobPortalUserRepository.findJobPortalUserByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(userMapper.mapToProfileDto(profile, true)).thenReturn(dto);

        // Act
        ProfileDto result = userService.getProfilePicture(user.getEmail());

        // Assert
        assertEquals(dto, result);
    }

    @Test
    void testGetResume() {
        // Arrange
        Profile profile = new Profile();
        user.setProfile(profile);

        ProfileDto dto = mock(ProfileDto.class);

        when(jobPortalUserRepository.findJobPortalUserByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(userMapper.mapToProfileDto(profile, true)).thenReturn(dto);

        // Act
        ProfileDto result = userService.getResume(user.getEmail());

        // Assert
        assertEquals(dto, result);
    }

    @Test
    void testSaveJob() {
        // Arrange
        JobDto dto = mock(JobDto.class);

        when(jobPortalUserRepository.findJobPortalUserByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));

        when(userMapper.transformJobToDto(job)).thenReturn(dto);

        // Act
        JobDto result = userService.saveJob(user.getEmail(), 1L);

        // Assert
        assertEquals(dto, result);
        assertTrue(user.getSavedJobs().contains(job));
    }

    @Test
    void testUnsaveJob() {
        // Arrange
        user.getSavedJobs().add(job);

        when(jobPortalUserRepository.findJobPortalUserByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));

        // Act
        userService.unsaveJob(user.getEmail(), 1L);

        // Assert
        assertFalse(user.getSavedJobs().contains(job));
    }

    @Test
    void testGetSavedJobs() {
        // Arrange
        user.getSavedJobs().add(job);

        JobDto dto = mock(JobDto.class);

        when(jobPortalUserRepository.findJobPortalUserByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(userMapper.transformJobToDto(job)).thenReturn(dto);

        // Act
        List<JobDto> result = userService.getSavedJobs(user.getEmail());

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void testApplyForJob() {
        // Arrange
        ApplyJobRequestDto request =
                new ApplyJobRequestDto(1L, "I am interested");

        JobApplication application = new JobApplication();

        JobApplicationDto dto = mock(JobApplicationDto.class);

        when(jobPortalUserRepository.findJobPortalUserByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(jobApplicationRepository.existsByUserIdAndJobId(1L, 1L))
                .thenReturn(false);

        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));

        when(jobApplicationRepository.save(any())).thenReturn(application);

        when(userMapper.mapToJobApplicationDto(application)).thenReturn(dto);

        // Act
        JobApplicationDto result =
                userService.applyForJob(user.getEmail(), request);

        // Assert
        assertEquals(dto, result);
        assertEquals(1, job.getApplicationsCount());
    }

    @Test
    void testWithdrawApplication() {
        // Arrange
        job.setApplicationsCount(1);

        when(jobPortalUserRepository.findJobPortalUserByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(jobApplicationRepository.existsByUserIdAndJobId(1L, 1L))
                .thenReturn(true);

        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));

        // Act
        userService.withdrawApplication(user.getEmail(), 1L);

        // Assert
        assertEquals(0, job.getApplicationsCount());

        verify(jobApplicationRepository)
                .deleteByUserIdAndJobId(1L, 1L);
    }

    @Test
    void testGetJobSeekerApplications() {
        // Arrange
        JobApplication application = new JobApplication();
        user.getJobApplications().add(application);

        JobApplicationDto dto = mock(JobApplicationDto.class);

        when(jobPortalUserRepository.findJobPortalUserByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(userMapper.mapToJobApplicationDto(application)).thenReturn(dto);

        // Act
        List<JobApplicationDto> result =
                userService.getJobSeekerApplications(user.getEmail());

        // Assert
        assertEquals(1, result.size());
    }
}