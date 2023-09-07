package com.example.petree.domain.adopter.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "residential_env_img_file")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResidentialEnvImgFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name ="native", strategy = "native")
    private Long id;
    @Enumerated(EnumType.STRING)
    private SpaceType spaceType;
    private String originalFileName;
    private String fileName;
    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adopter_id")
    private Adopter adopter;

    public ResidentialEnvImgFile(SpaceType spaceType, String originalFileName, String fileName, String fileUrl, Adopter adopter) {
        this.spaceType = spaceType;
        this.originalFileName = originalFileName;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.adopter = adopter;
    }
}
