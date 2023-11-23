package com.example.petree.domain.breeder.domain;

import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * packageName    : com.example.petree.domain.breeder.domain
 * fileName       : BreederProjectionImpl
 * author         : 박수현
 * date           : 2023-07-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-13        박수현              최초 생성
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BreederProjectionImpl implements BreederProjection {
    private Long id;
    private String email;
    private String nickname;
    private String phoneNumber;
    private String address1;
    private Boolean isVerified;
    private Double distance;
    private String types;

    public BreederProjectionImpl(Long id,
                                 String email,
                                 String nickname,
                                 String phoneNumber,
                                 String address,
                                 Boolean isVerified,
                                 String types){
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.address1 = address1;
        this.isVerified = isVerified;
        this.types = types;
    }
}

