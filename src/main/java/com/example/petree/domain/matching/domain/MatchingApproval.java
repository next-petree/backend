package com.example.petree.domain.matching.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "matching_approval")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MatchingApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name ="native", strategy = "native")
    private Long id;
    private Boolean isApproved;

    public MatchingApproval(Boolean isApproved) {
        this.isApproved = isApproved;
    }
}
