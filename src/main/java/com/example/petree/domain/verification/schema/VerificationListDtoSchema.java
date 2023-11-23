package com.example.petree.domain.verification.schema;

import com.example.petree.domain.verification.dto.VerificationListDto;
import com.example.petree.global.JsendStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;



public class VerificationListDtoSchema {
    @Data
    public static class VerificationListDto200{
        @Schema(example = "SUCCESS")
        private JsendStatus status;
        @Schema
        VerificationListDto data;
    }

}