package com.example.BE.repository;

import com.example.BE.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job,Long> {
    List<Job> findByStatusOrderByPostedDateDesc(String status);
}
