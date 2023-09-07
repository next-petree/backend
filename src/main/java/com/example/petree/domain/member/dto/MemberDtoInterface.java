package com.example.petree.domain.member.dto;

import com.example.petree.domain.member.domain.Role;

/**
 * packageName    : com.example.petree.domain.member.dto
 * fileName       : MemberDtoInterface
 * author         : 박수현
 * date           : 2023-07-02
 * description    : MemberDtoInterface
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-02        박수현              최초 생성
 */
public interface MemberDtoInterface {

    Long getId();
    String getMemberId();
    String getNickname();
    String getPassword();
    String getPhoneNumber();
    String getAddress();
    Double getLatitude();
    Double getLongitude();
    Role getRole();
}
