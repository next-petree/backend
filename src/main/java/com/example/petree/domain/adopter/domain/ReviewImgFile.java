package com.example.petree.domain.adopter.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * packageName    : com.example.petree.domain.adopter.domain
 * fileName       : ReviewImgFile
 * author         : 박수현
 * date           : 2023-08-28
 * description    : 분양후기 이미지
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-28        박수현              최초 생성
 */

@Entity
@Table(name = "review_img_file")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewImgFile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name ="native", strategy = "native")
    private Long id;
    private String originalFileName;
    private String fileName;
    private String fileUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;
}
