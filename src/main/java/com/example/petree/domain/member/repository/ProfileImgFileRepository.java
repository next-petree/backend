package com.example.petree.domain.member.repository;

import com.example.petree.domain.member.domain.Member;
import com.example.petree.domain.member.domain.ProfileImgFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileImgFileRepository extends JpaRepository<ProfileImgFile, Long> {
    Optional<ProfileImgFile> findByMember(Member member);
}
