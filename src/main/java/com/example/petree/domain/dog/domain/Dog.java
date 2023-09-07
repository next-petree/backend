package com.example.petree.domain.dog.domain;

import com.example.petree.domain.breeder.domain.Breeder;
import com.example.petree.domain.dog.dto.PossessionDogDto;
import com.example.petree.domain.main_breed.domain.DogType;
import com.example.petree.domain.main_breed.domain.MainBreed;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jboss.jandex.Main;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.parameters.P;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

/**
 * packageName    : com.example.petree.global
 * fileName       : TemplateController
 * author         : 이지수
 * date           : 2023-07-05
 * description    : Dog
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-05        이지수     추가 작성
 * 2023-07-08        이지수     보유 견종 CRUD 작성
 * 2023-07-11        이지수     보유 견종 파일 업로드 작성
 * 2023-07-31        이지수     swagger 작성, 수정 시 발생한 오류 해결을 위한
 *                             setDog 작성 (아직 LocalDate->null로 반환됨)
 * 2023-08-02        이지수     LocalDate 수정 완료
 * 2023-08-29        이지수          create, update 분리
 */

@Entity
@Table(name = "Dog")
@NoArgsConstructor
@Getter
@Setter
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Dog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Status status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private LocalDate createDate;

    private String name;
    @Column(length = 2000)
    private String management;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dog_type_id")
    private DogType dogType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "breeder_id", referencedColumnName = "id", nullable = false, updatable = false)
    private Breeder breeder;

    @OneToMany(mappedBy = "dog", cascade = CascadeType.ALL)
    private List<DogImgFile> dogImgFiles = new ArrayList<>();

    public Dog(Breeder breeder, PossessionDogDto.CreateDogDto possessionDogDto,LocalDate createDate) {
        this.breeder = breeder;
        this.birthDate = birthDate;
        this.gender = gender;
        this.name = name;
        this.management = management;
        this.createDate=createDate;
    }



    public void addDogImgFile(DogImgFile dogImgFile) {
        dogImgFiles.add(dogImgFile);}

    public void createFromDto(PossessionDogDto.CreateDogDto createDogDto) {
        setGender(createDogDto.getGender());
        setBirthDate(createDogDto.getBirthDate());
        setName(createDogDto.getName());
        setManagement(createDogDto.getManagement());
    }
    public void updateFromDto(PossessionDogDto.UpdateDogDto updateDogDto){
        this.gender=updateDogDto.getGender();
        this.birthDate = updateDogDto.getBirthDate();
        this.name=updateDogDto.getName();
        this.management= updateDogDto.getManagement();
        this.status=updateDogDto.getStatus();
    }


    public void setStatus(Status status){this.status=status;}
    public void setDogType(DogType dogType){this.dogType=dogType;}
}