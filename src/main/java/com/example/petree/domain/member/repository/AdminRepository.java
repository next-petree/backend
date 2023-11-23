package com.example.petree.domain.member.repository;

import com.example.petree.domain.member.domain.Admin;
import com.example.petree.domain.member.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {
    Admin findByRole(Role role);
}
