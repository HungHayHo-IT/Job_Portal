package com.example.BE.repository;

import com.example.BE.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    boolean existsByUserIdAndJobId(Long userId, Long jobId);

    // Delete an application by user ID and job ID
    void deleteByUserIdAndJobId(Long userId, Long jobId);

    // Find all applications by user ID
    List<JobApplication> findByUserIdOrderByAppliedAtDesc(Long userId);

    // Find applications by job ID
    List<JobApplication> findByJobIdOrderByAppliedAtAsc(Long jobId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
        UPDATE JobApplication j
        SET j.status = :status,
            j.notes = :notes,
            j.updatedAt = :updatedAt,
            j.updatedBy = :updatedBy
        WHERE j.id = :id
        """)
    int updateStatusAndNotesById(
            @Param("status") String status,
            @Param("notes") String notes,
            @Param("id") Long id,
            @Param("updatedBy") String updatedBy,
            @Param("updatedAt") Instant updatedAt
    );


}
