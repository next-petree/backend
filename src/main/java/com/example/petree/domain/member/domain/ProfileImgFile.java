package com.example.petree.domain.member.domain;

import com.example.petree.domain.dog.domain.Dog;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * packageName    : com.example.petree.member
 * fileName       : ProfileImgFile
 * author         : 박수현
 * date           : 2023-06-30
 * description    : originalFilename은 사용자가 이미지를 첨부했을 때의 원래 파일이름으로,
 *                  (예를 들어 kakaoScreenshot1.jpg 같은 형식),
 *                  이는 이름이 겹칠 우려가 있음.
 *                  그렇기 때문에 s3같은 원격 스토리지에 실제로 저장되는 파일은
 *                  uuid같은 임의의 값으로 설정을 하는데 이것이 fileName.
 *                  fileUrl은 이제 s3에 저장된 경로.
 *                  프론트쪽에 이미지를 넘겨줄 때는 이 fileUrl을 보내주면 됨.
 *                  프론프는 이를 받아서 <img src={fileUrl} ~/>  이런식으로 처리되는 방식
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-30        박수현              최초 생성
 */

@Entity
@Table(name = "profile_img_file")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileImgFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name ="native", strategy = "native")
    private Long id;
    private String originalFileName;
    private String fileName;
    private String fileUrl;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    private Member member;
}
