package com.example.BE.repository;

import com.example.BE.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {

    @Query("select distinct c from Company c join fetch c.jobs j where j.status = :status")
    List<Company> findAllWithJobsByStatus(@Param("status") String status);
}
