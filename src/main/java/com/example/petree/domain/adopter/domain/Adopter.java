package com.example.petree.domain.adopter.domain;

import com.example.petree.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : com.example.petree.domain.member.domain
 * fileName       : Adopter
 * author         : 박수현
 * date           : 2023-08-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-07        박수현              최초 생성
 */
@Entity
@Table(name = "adopter")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class Adopter extends Member {

    @ColumnDefault("false")
    private Boolean isVerified;

    @OneToMany(mappedBy = "adopter", cascade = CascadeType.ALL)
    private List<ResidentialEnvImgFile> residentialEnvImgFiles = new ArrayList<>();


}
