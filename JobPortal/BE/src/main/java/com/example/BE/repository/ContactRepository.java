package com.example.BE.repository;

import com.example.BE.entity.Contact;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Long> {
    List<Contact> findContactsByStatus(String status);

    List<Contact> findContactsByStatus(String status, Sort sort);

    List<Contact> findContactsByStatus(String status, Pageable pageable);
}
