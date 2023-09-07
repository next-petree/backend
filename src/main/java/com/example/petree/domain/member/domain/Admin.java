package com.example.petree.domain.member.domain;

import com.example.petree.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "admin")
@Getter
@NoArgsConstructor
@SuperBuilder
public class Admin extends Member {

    //@ColumnDefault("0")
    //private Boolean isVerified;


}
