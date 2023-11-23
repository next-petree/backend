package com.example.petree.domain.verification.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "attached_file")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachedFile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name ="native", strategy = "native")
    private Long id;
    private String originalFileName;
    private String fileName;
    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "req_id")
    private VerificationRequest verificationRequest;

    public String getFileUrl() {
        return this.fileUrl;
    }

    public AttachedFile(String originalName, String filename, String fileUrl, VerificationRequest verificationRequest) {
    this.originalFileName =originalName;
    this.fileName=filename;
    this.fileUrl=fileUrl;
    this.verificationRequest=verificationRequest;
    }
}
