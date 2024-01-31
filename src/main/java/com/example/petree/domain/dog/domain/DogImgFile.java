package com.example.petree.domain.dog.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;


import javax.persistence.*;

@Entity
@Table(name = "dog_img_file")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
}