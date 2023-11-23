package com.example.petree.domain.main_breed.domain;

import com.example.petree.domain.breeder.domain.Breeder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : com.example.petree.domain.main_breed.domain
 * fileName       : BreedType
 * author         : jsc
 * date           : 2023/08/11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/11        jsc
 */
@Entity
@Table(name = "dog_type")
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Data
public class DogType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private Size size;

    @Column(length = 400)
    private String imgUrl;
}
