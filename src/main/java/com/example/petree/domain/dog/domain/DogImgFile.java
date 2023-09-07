package com.example.petree.domain.dog.domain;

import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;


import javax.persistence.*;

@Entity
@Table(name = "dog_img_file")
@Getter
public class DogImgFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name ="native", strategy = "native")
    private Long id;
    private String originalFileName;
    private String fileName;
    private String fileUrl;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dog_id")
    private Dog dog;

    public DogImgFile (String originalFileName, String fileName, String fileUrl,Dog dog) {
        this.originalFileName= originalFileName;
        this.fileName= fileName;
        this.fileUrl=fileUrl;
        this.dog=dog;
    }
    public DogImgFile() {
    }
}