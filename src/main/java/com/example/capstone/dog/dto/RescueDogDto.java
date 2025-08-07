package com.example.capstone.dog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RescueDogDto {
    private String desertionNo;
    private String kindCd;
    private String sexCd;
    private String age;
    private String weight;
    private String specialMark;
    private String happenPlace;
    private String processState;
    private String careNm;
    private String careTel;
    private String noticeSdt;
    private String noticeEdt;

    @JsonProperty("popfile1")
    private String filename;

    private String size;
}