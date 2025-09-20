package com.example.capstone.dog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeNameDto {
    private String code; // upr_cd or org_cd
    private String name; // 시도명 / 시군구명
}