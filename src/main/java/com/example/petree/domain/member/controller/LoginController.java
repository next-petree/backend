package com.example.petree.domain.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * packageName    : com.example.petree.domain.verification
 * fileName       : LoginController
 * author         : qkrtn_ulqpbq2
 * date           : 2023-07-12
 * description    : 임시 생성
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-12        qkrtn_ulqpbq2       최초 생성
 */

@Controller
public class LoginController {

    /**
     * @author 박수현
     * @date 2023-07-12
     * @description : 임시 생성 --
     * @return
     */

    @GetMapping("/loginForm")
    public String login() {
        return "loginForm";
    }
}
