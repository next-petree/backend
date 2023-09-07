package com.example.petree.domain.adopter.repository;

import com.example.petree.domain.adopter.domain.Adopter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdopterRepository extends JpaRepository<Adopter, Long> {

    Optional<Adopter> findByEmail(String email);

}
