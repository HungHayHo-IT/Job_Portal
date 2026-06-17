package com.example.BE.controller;

import com.example.BE.dto.*;
import com.example.BE.user.controller.UserController;
import com.example.BE.user.service.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private IUserService iUserService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String email = "test@gmail.com";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    void testSearchUserByEmail() throws Exception {
        // Arrange
        UserDto userDto = mock(UserDto.class);

        when(iUserService.searchUserByEmail(email))
                .thenReturn(Optional.of(userDto));

        // Act + Assert
        mockMvc.perform(get("/api/v1/users/search/admin")
                        .param("email", email))
                .andExpect(status().isOk());
    }

    @Test
    void testSearchUserByEmailNotFound() throws Exception {
        // Arrange
        when(iUserService.searchUserByEmail(email))
                .thenReturn(Optional.empty());

        // Act + Assert
        mockMvc.perform(get("/api/v1/users/search/admin")
                        .param("email", email))
                .andExpect(status().isNotFound());
    }

    @Test
    void testElevateToEmployer() throws Exception {
        // Arrange
        UserDto dto = mock(UserDto.class);

        when(iUserService.elevateToEmployer(1L)).thenReturn(dto);

        // Act + Assert
        mockMvc.perform(patch("/api/v1/users/1/role/employer/admin"))
                .andExpect(status().isOk());
    }

    @Test
    void testAssignCompanyToEmployer() throws Exception {
        // Arrange
        UserDto dto = mock(UserDto.class);

        when(iUserService.assignCompanyToEmployer(1L, 1L))
                .thenReturn(dto);

        // Act + Assert
        mockMvc.perform(patch("/api/v1/users/1/company/1/admin"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateOrUpdateProfile() throws Exception {
        // Arrange
        ProfileDto dto = mock(ProfileDto.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(email);
        when(iUserService.createOrUpdateProfile(anyString(), anyString(), any(), any()))
                .thenReturn(dto);

        MockMultipartFile profile =
                new MockMultipartFile(
                        "profile",
                        "",
                        "application/json",
                        "{\"jobTitle\":\"Java Dev\"}".getBytes()
                );

        MockMultipartFile picture =
                new MockMultipartFile(
                        "profilePicture",
                        "pic.jpg",
                        "image/jpeg",
                        "abc".getBytes()
                );

        // Act + Assert
        mockMvc.perform(multipart("/api/v1/users/profile/jobseeker")
                        .file(profile)
                        .file(picture)
                        .principal(authentication)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk());
    }

    @Test
    void testGetProfile() throws Exception {
        // Arrange
        ProfileDto dto = mock(ProfileDto.class);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(email);
        when(iUserService.getProfile(email)).thenReturn(dto);

        // Act + Assert
        mockMvc.perform(get("/api/v1/users/profile/jobseeker")
                        .principal(authentication))
                .andExpect(status().isOk());
    }

    @Test
    void testGetProfilePicture() throws Exception {
        // Arrange
        ProfileDto dto = mock(ProfileDto.class);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(email);

        when(dto.profilePicture()).thenReturn("abc".getBytes());
        when(dto.profilePictureType()).thenReturn("image/jpeg");

        when(iUserService.getProfilePicture(email)).thenReturn(dto);

        // Act + Assert
        mockMvc.perform(get("/api/v1/users/profile/picture/jobseeker")
                        .principal(authentication))
                .andExpect(status().isOk());
    }

    @Test
    void testGetResume() throws Exception {
        // Arrange
        ProfileDto dto = mock(ProfileDto.class);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(email);

        when(dto.resume()).thenReturn("resume".getBytes());
        when(dto.resumeType()).thenReturn("application/pdf");
        when(dto.resumeName()).thenReturn("cv.pdf");

        when(iUserService.getResume(email)).thenReturn(dto);

        // Act + Assert
        mockMvc.perform(get("/api/v1/users/profile/resume/jobseeker")
                        .principal(authentication))
                .andExpect(status().isOk());
    }

    @Test
    void testSaveJob() throws Exception {
        // Arrange
        JobDto dto = mock(JobDto.class);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(email);

        when(iUserService.saveJob(email, 1L)).thenReturn(dto);

        // Act + Assert
        mockMvc.perform(post("/api/v1/users/saved-jobs/1/jobseeker")
                        .principal(authentication))
                .andExpect(status().isCreated());
    }
    @Test
    void testUnsaveJob() throws Exception {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(email);

        doNothing().when(iUserService).unsaveJob(email, 1L);

        // Act + Assert
        mockMvc.perform(delete("/api/v1/users/saved-jobs/1/jobseeker")
                        .principal(authentication))
                .andExpect(status().isOk());
    }

    @Test
    void testGetSavedJobs() throws Exception {
        // Arrange
        when(iUserService.getSavedJobs(email))
                .thenReturn(List.of(mock(JobDto.class)));
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(email);
        // Act + Assert
        mockMvc.perform(get("/api/v1/users/saved-jobs/jobseeker")
                        .principal(authentication))
                .andExpect(status().isOk());
    }

    @Test
    void testApplyForJob() throws Exception {
        // Arrange
        JobApplicationDto dto = mock(JobApplicationDto.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(email);
        ApplyJobRequestDto request =
                new ApplyJobRequestDto(1L, "Interested");

        when(iUserService.applyForJob(eq(email), any()))
                .thenReturn(dto);

        // Act + Assert
        mockMvc.perform(post("/api/v1/users/job-applications/jobseeker")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void testWithdrawApplication() throws Exception {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(email);
        doNothing().when(iUserService)
                .withdrawApplication(email, 1L);


        // Act + Assert
        mockMvc.perform(delete("/api/v1/users/job-applications/1/jobseeker")
                        .principal(authentication))
                .andExpect(status().isOk());
    }

    @Test
    void testGetJobSeekerApplications() throws Exception {
        // Arrange
        when(iUserService.getJobSeekerApplications(email))
                .thenReturn(List.of(mock(JobApplicationDto.class)));
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(email);
        // Act + Assert
        mockMvc.perform(get("/api/v1/users/job-applications/jobseeker")
                        .principal(authentication))
                .andExpect(status().isOk());
    }
}