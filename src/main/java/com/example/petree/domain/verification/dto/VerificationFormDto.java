package com.example.petree.domain.verification.dto;

import com.example.petree.domain.verification.domain.Certification;
import com.example.petree.domain.verification.domain.Status;
import com.example.petree.domain.verification.domain.VerificationRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
/**
 * @author 이지수
 * @date 2023-08-08
 * @description : 브리더 자격증 종류 enum 추가
 * @return certification
 */
@Data
public class VerificationFormDto {

    @Schema(description = "자격증 분류 선택 (반려동물종합관리사 OR 반려동물행동교정사)",example = "COMPREHENSIVE_PET_MANAGER")
    private Certification certification;

    @Schema(description = "브리더 증명 파일 업로드")
    @JsonSerialize(using = ToStringSerializer.class)
    private MultipartFile verificationFiles;


}
