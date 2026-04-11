package com.example.BE.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PathConfig {

    @Bean(name = "publicPaths")
    public static List<String> publicPath(){
        return List.of(
                "api/v1/companies/public",
                "api/v1/csrf-token/public",
                "api/v1/auth/register/public",
                "api/v1/auth/login/public",
                "api/v1/contacts/public",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-ui.html",
                "/webjars/**",
                "/actuator/**",
                "/todos/**"
        );
    }
    @Bean
    public static List<String> securePath(){
        return List.of(

                "/api/**"
        );
    }
    @Bean(name = "employerPaths")
    public static List<String> employerPaths() {
        return List.of(
                "/api/v1/jobs/employer",
                "/api/v1/jobs/${jobId}/status/employer",
                "/api/v1/jobs/applications/{jobId}/employer",
                "/api/v1/jobs/applications/employer"
        );
    }

    @Bean(name = "jobseekerPaths")
    public static List<String> jobseekerPaths() {
        return List.of(
                "/api/v1/users/profile/jobseeker",
                "/api/v1/users/profile/picture/jobseeker",
                "/api/v1/users/profile/resume/jobseeker",
                "/api/v1/users/saved-jobs/${jobId}/jobseeker",
                "/api/v1/users/saved-jobs/jobseeker",
                "/api/v1/users/job-applications/jobseeker",
                "/api/v1/users/job-applications/${jobId}/jobseeker",
                "/api/v1/users/saved-jobs/{jobId}/jobseeker",
                "/api/v1/users/saved-jobs/jobseeker",
                "/api/v1/users/job-applications/jobseeker",
                "/api/v1/users/job-applications/{jobId}/jobseeker",
                "/api/v1/users/job-applications/jobseeker"
        );
    }


    @Bean(name = "adminPaths")
    public static List<String> adminPaths(){
        return List.of(
                "/api/v1/companies/admin",
                "/api/v1/companies/${id}/admin",
                "/api/v1/contacts/${id}/status/admin",
                "/api/v1/contacts/page/admin",
                "/api/v1/contacts/sort/admin",
                "/api/v1/contacts/admin",
                "/api/v1/users/${userId}/role/employer/admin",
                "/api/v1/users/${userId}/company/{companyId}/admin"
        );
    }
}
