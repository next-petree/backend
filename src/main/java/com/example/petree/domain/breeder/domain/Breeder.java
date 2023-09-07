package com.example.petree.domain.breeder.domain;

import com.example.petree.domain.dog.domain.Dog;
import com.example.petree.domain.main_breed.domain.DogType;
import com.example.petree.domain.main_breed.domain.MainBreed;
import com.example.petree.domain.member.domain.Member;
import com.example.petree.domain.member.domain.Role;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "breeder")
@NoArgsConstructor
@Getter
@SuperBuilder
public class Breeder extends Member {

    @ColumnDefault("0")
    private Boolean isVerified;

    @OneToMany(mappedBy = "breeder", cascade = CascadeType.ALL)
    private List<MainBreed> mainBreeds = new ArrayList<>();

    @OneToMany(mappedBy = "breeder", cascade = CascadeType.ALL)
    private List<Dog> dogs = new ArrayList<>();

    public Breeder(Long id, String email, String nickname, String password, String phoneNumber, String address, Double latitude, Double longitude, Role role, Boolean isVerified) {
        //super(id, memberId, nickname, password, phoneNumber, address, latitude, longitude, role);
        this.isVerified = isVerified;
    }

    public void setMainBreeds(List<MainBreed> mainBreeds) {
        this.mainBreeds = mainBreeds;
    }


    /**
     * @author 이지수
     * @date 2023-08-07
     * @description : 브리더 인증에 성공하면, isVerified -> true로
     * @return isVerified
     */
    public void setIsVerified(Boolean isVerified){
        this.isVerified=isVerified;
    }

}
