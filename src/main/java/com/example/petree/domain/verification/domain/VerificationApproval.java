package com.example.petree.domain.verification.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "VerificationApproval")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name ="native", strategy = "native")
    private Long id;
    private Boolean isApproved;

    public VerificationApproval(Boolean isApproved) {
        this.isApproved=isApproved;
    }
}
