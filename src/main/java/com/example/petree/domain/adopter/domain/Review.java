package com.example.petree.domain.adopter.domain;

import com.example.petree.domain.dog.domain.Dog;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : com.example.petree.domain.adopter.domain
 * fileName       : AdoptionReview
 * author         : 박수현
 * date           : 2023-08-28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-28        박수현              최초 생성
 */

@Entity
@Table(name = "review")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dog_id")
    private Dog dog;

    @CreationTimestamp
    private LocalDate writeDate;

    @ManyToOne
    @JoinColumn(name = "adopter_id")
    private Adopter adopter;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReviewImgFile> images = new ArrayList<>();
}
