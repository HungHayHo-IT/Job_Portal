package com.example.BE.repository;

import com.example.BE.entity.JobPortalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobPortalUserRepository extends JpaRepository<JobPortalUser,Long> {

    Optional<JobPortalUser> findJobPortalUserByEmail(String email);

    Optional<JobPortalUser> readUserByEmailOrMobileNumber(String email, String mobileNumber);
}
