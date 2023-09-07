package com.example.petree.domain.matching.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "pledge")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Pledge {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name ="native", strategy = "native")
    private Long id;

    private String nurturingEnv;

    public Pledge(String nurturingEnv, String parentExp) {
        this.nurturingEnv = nurturingEnv;
        this.parentExp = parentExp;
    }

    private String parentExp;

}
