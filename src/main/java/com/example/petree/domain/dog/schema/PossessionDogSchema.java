package com.example.petree.domain.dog.schema;

import com.example.petree.domain.dog.dto.PossessionDogDto;
import com.example.petree.global.JsendStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PossessionDogSchema {
    @Data
    public static class PosessionDogSchema200{
        @Schema(example = "SUCCESS")
        private JsendStatus status;
        private PossessionDogDto data;
    }


}

