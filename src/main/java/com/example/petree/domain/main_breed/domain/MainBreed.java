package com.example.petree.domain.main_breed.domain;

import com.example.petree.domain.breeder.domain.Breeder;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

/**
 * packageName    : com.example.petree.domain.main_breed.domain
 * fileName       : MainBreed
 * author         : 박수현
 * date           : 2023-08-14
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-14        박수현              최초 생성
 */

@Entity
@Table(name = "main_breed")
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Data
@Builder
public class MainBreed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "breeder_id")
    private Breeder breeder;

    @ManyToOne
    @JoinColumn(name = "dog_type_id")
    private DogType dogType;
}
