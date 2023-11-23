package com.example.petree.domain.verification.schema;

import com.example.petree.domain.verification.dto.VerificationFormDto;
import com.example.petree.global.JsendStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

public class VerificationSchema {
    @Data
    public static class Verification200{
        @Schema(example = "SUCCESS")
        private JsendStatus status;
        @Schema
        VerificationFormDto data;
    }

}
