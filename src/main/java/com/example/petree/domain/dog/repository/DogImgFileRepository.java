package com.example.petree.domain.dog.repository;

import com.example.petree.domain.dog.domain.Dog;
import com.example.petree.domain.dog.domain.DogImgFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DogImgFileRepository extends JpaRepository<DogImgFile, Long> {
    List<DogImgFile> findByDogId(Long id);

    void deleteByIdAndDogId(Long fileId,Long dogId);

    DogImgFile findByIdAndDogId(Long fileId,Long dogId);
}
