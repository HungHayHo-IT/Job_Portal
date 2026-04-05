package com.example.BE.repository;

import com.example.BE.entity.Role;
import org.hibernate.annotations.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    @Cacheable("roles")
    Optional<Role> findRoleByName(String name);
}
