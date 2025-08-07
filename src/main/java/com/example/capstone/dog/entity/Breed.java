package com.example.capstone.dog.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Breed {

    @Id
    private String kindCd; // 품종 코드 (기본키)

    private String kindName; // 품종 이름
}