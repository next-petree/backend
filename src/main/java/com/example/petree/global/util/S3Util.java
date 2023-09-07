package com.example.petree.global.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * packageName    : com.example.petree.global.util
 * fileName       : S3Util
 * author         : jsc
 * date           : 2023/07/09
 * description    : S3 작업 관련 메서드
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/09        정세창               init
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class S3Util {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * @author 박수현
     * @date 2023-08-03
     * @description : MultipartFile - 업로드하려는 파일의 내용을 나타내는 객체,
     *                String dirName - S3버킷내에서 파일이 저장될 디렉토리 이름,
     *                String fileName - S3에 저장될 파일 이름 -> UUID로 고유한 파일 이름 생성
     *
     * @return String
     */

    public String upload(MultipartFile multipartFile, String dirName, String fileName) {
        String s3Path = dirName + "/" + fileName;
        try {
            return putS3(multipartFile, s3Path);
        } catch (IOException e) {
            throw new AmazonS3Exception("S3 업로드 실패");
        }

    }

    // S3에서 해당 파일 삭제
    public void delete(String s3Path) {
        amazonS3Client.deleteObject(bucket, s3Path);
        log.info("S3에서 삭제된 파일: {}", s3Path);
    }

    /**
     * @author 박수현
     * @date 2023-08-03
     * @description : s3에 파일업로드, 파일의 사이즈를 ContentLength로 S3에 알려주기 위해서 ObjectMetadata를 사용
     * @return String
     */

    private String putS3(MultipartFile multipartFile, String s3Path) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());
        //S3 API 메소드인 putObject를 이용하여 파일 Stream을 열어서 S3에 파일을 업로드
        amazonS3Client.putObject(new PutObjectRequest(bucket, s3Path, multipartFile.getInputStream(), objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        //getUrl 메소드를 통해서 S3에 업로드된 사진 URL을 가져오는 방식
        return amazonS3Client.getUrl(bucket, s3Path).toString();
    }
}
