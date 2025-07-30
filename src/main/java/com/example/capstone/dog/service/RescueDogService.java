package com.example.capstone.dog.service;

import com.example.capstone.dog.client.OpenApiClient;
import com.example.capstone.dog.dto.RescueDogDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RescueDogService {

    private final OpenApiClient openApiClient;
    private final ObjectMapper objectMapper;

    public List<RescueDogDto> getDogs(int page) {
        String json = openApiClient.fetchRawJson(page); // sidoCode 파라미터 제거
        List<RescueDogDto> dogs = new ArrayList<>();

        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode items = root.path("response").path("body").path("items").path("item");

            if (items.isArray()) {
                for (JsonNode item : items) {
                    if (!"417000".equals(item.path("upKindCd").asText())) continue;

                    RescueDogDto dog = new RescueDogDto();
                    dog.setDesertionNo(item.path("desertionNo").asText());
                    dog.setKindCd(item.path("kindCd").asText());
                    dog.setSexCd(item.path("sexCd").asText());
                    dog.setAge(item.path("age").asText());
                    dog.setWeight(item.path("weight").asText());
                    dog.setSpecialMark(item.path("specialMark").asText());
                    dog.setHappenPlace(item.path("happenPlace").asText());
                    dog.setProcessState(item.path("processState").asText());
                    dog.setCareNm(item.path("careNm").asText());
                    dog.setCareTel(item.path("careTel").asText());
                    dog.setFilename(item.path("popfile1").asText());
                    dog.setNoticeSdt(item.path("noticeSdt").asText());
                    dog.setNoticeEdt(item.path("noticeEdt").asText());

                    dogs.add(dog);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("유기동물 데이터를 파싱하는 중 오류가 발생했습니다.", e);
        }

        return dogs;
    }
}