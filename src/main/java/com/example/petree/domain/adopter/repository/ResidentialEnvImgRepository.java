package com.example.petree.domain.adopter.repository;

import com.example.petree.domain.adopter.domain.Adopter;
import com.example.petree.domain.adopter.domain.ResidentialEnvImgFile;
import com.example.petree.domain.adopter.domain.SpaceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResidentialEnvImgRepository extends JpaRepository<ResidentialEnvImgFile, Long> {

    List<ResidentialEnvImgFile> findAllByAdopter(Adopter adopter);

    Optional<ResidentialEnvImgFile> findByAdopterAndSpaceType(Adopter adopter, SpaceType spaceType);
}
