package com.example.petree.domain.breeder.domain;

import java.util.List;
import java.util.Set;

/**
 * packageName    : com.example.petree.domain.breeder.domain
 * fileName       : BreederProjection
 * author         : 박수현
 * date           : 2023-07-04
 * description    : BreederProjection
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-04        박수현              최초 생성
 */
public interface BreederProjection {
    Long getId();

    String getEmail();
    String getNickname();

    //@Value("#{target.phone_number}")
    String getPhoneNumber();
    String getAddress1();

    //@Value("#{target.is_verified}")
    Boolean getIsVerified();

    Double getDistance();

    //@Value("#{target.types}")
    String getTypes();

}

