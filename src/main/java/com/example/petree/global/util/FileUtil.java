package com.example.petree.global.util;

import org.springframework.stereotype.Component;

/**
 * packageName    : com.example.petree.global.util
 * fileName       : FileUtil
 * author         : jsc
 * date           : 2023/07/09
 * description    : 파일 관련 작업 시의 유틸 메소드
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/09        jsc
 */

@Component
public class FileUtil {

    /**
     * @author 박수현
     * @date 2023-08-03
     * @description : 파일 이름에서 확장자(extension)를 추출하는 유틸리티 클래스
     * @return String
     */

    public String extractExt(String filename) {
        int index = filename.lastIndexOf(".");
        return filename.substring(index+1);
    }
}
