package com.example.petree.domain.matching.domain;

import com.example.petree.domain.dog.domain.Dog;
import com.example.petree.domain.breeder.domain.Breeder;
import com.example.petree.domain.adopter.domain.Adopter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "matching")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Matching {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name ="native", strategy = "native")
    private Long id;

    private LocalDate submitDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "dog_id")
    private Dog dog;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="adopter_id")
    private Adopter adopter;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="breeder_id")
    private Breeder breeder;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="pledge_id")
    private Pledge pledge;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "matching_approval_id")
    private MatchingApproval matchingApproval;

    public void setPledge(Pledge pledge) {
        this.pledge = pledge;
    }

    public void setMatchingApproval(MatchingApproval matchingApproval) {
        this.matchingApproval = matchingApproval;
    }

    public Matching(LocalDate submitDate, Dog dog, Adopter adopter, Breeder breeder) {
        this.submitDate = submitDate;
        this.dog = dog;
        this.adopter = adopter;
        this.breeder = breeder;
    }
}

