package com.example.capstone.dog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BreedDto {

    @JsonProperty("kindCd")
    private String kindCd;

    @JsonProperty("knm")
    private String kindName;
}