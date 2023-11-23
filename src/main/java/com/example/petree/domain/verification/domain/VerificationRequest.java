package com.example.petree.domain.verification.domain;

import com.example.petree.domain.member.domain.Admin;
import com.example.petree.domain.breeder.domain.Breeder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "verification_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class VerificationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name ="native", strategy = "native")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Certification certification;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="breeder_id")
    private Breeder breeder;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="admin_id")
    private Admin admin;

    private LocalDate submitDate;

    @OneToOne
    @JoinColumn(name = "approval_id")
    private VerificationApproval verificationApproval;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "verificationRequest",cascade = CascadeType.ALL)
    private List<AttachedFile> verificationFiles = new ArrayList<>();

    public VerificationRequest(LocalDate submitDate, Breeder breeder) {
        this.submitDate= submitDate;
        this.breeder= breeder;
    }

    public void addAttachedFile(AttachedFile attachedFile) {
        this.verificationFiles.add(attachedFile);}

    public void setStatus(Status status){this.status=status;}

    public  void setCertification(Certification certification){this.certification=certification;}

    public void setVerificationApproval(VerificationApproval verificationApproval){this.verificationApproval=verificationApproval;}

}
