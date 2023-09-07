package com.example.petree.domain.dog.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {

    MALE("수컷"),
    FEMALE("암컷");

    private final String gender;
}
