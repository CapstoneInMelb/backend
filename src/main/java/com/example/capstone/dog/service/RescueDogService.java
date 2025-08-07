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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RescueDogService {

    private final OpenApiClient openApiClient;
    private final ObjectMapper objectMapper;

    public List<RescueDogDto> getDogs(int page, String kindCd, String size) {
        String json = openApiClient.fetchRawJson(page, kindCd);
        List<RescueDogDto> dogs = parseDogsFromJson(json);

        if (size != null && !size.trim().isEmpty()) {
            return dogs.stream()
                    .filter(dog -> size.equalsIgnoreCase(dog.getSize()))
                    .collect(Collectors.toList());
        }
        return dogs;
    }

    private List<RescueDogDto> parseDogsFromJson(String json) {
        List<RescueDogDto> dogs = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode items = root.path("response").path("body").path("items").path("item");

            if (items.isArray()) {
                for (JsonNode item : items) {
                    dogs.add(createDogDtoFromNode(item));
                }
            } else if (items.isObject()) {
                dogs.add(createDogDtoFromNode(items));
            }
        } catch (Exception e) {
            log.error("유기동물 데이터 파싱 중 오류 발생", e);
            throw new RuntimeException("유기동물 데이터를 파싱하는 중 오류가 발생했습니다.", e);
        }
        return dogs;
    }

    private RescueDogDto createDogDtoFromNode(JsonNode item) {
        RescueDogDto dog = new RescueDogDto();
        dog.setDesertionNo(item.path("desertionNo").asText());
        dog.setKindCd(item.path("kindCd").asText());
        dog.setSexCd(item.path("sexCd").asText());
        dog.setAge(item.path("age").asText());
        String weightStr = item.path("weight").asText();
        dog.setWeight(weightStr);
        dog.setSpecialMark(item.path("specialMark").asText());
        dog.setHappenPlace(item.path("happenPlace").asText());
        dog.setProcessState(item.path("processState").asText());
        dog.setCareNm(item.path("careNm").asText());
        dog.setCareTel(item.path("careTel").asText());
        dog.setFilename(item.path("popfile").asText());
        dog.setNoticeSdt(item.path("noticeSdt").asText());
        dog.setNoticeEdt(item.path("noticeEdt").asText());
        dog.setSize(getDogSize(weightStr));
        return dog;
    }

    private String getDogSize(String weightStr) {
        if (weightStr == null || weightStr.trim().isEmpty()) return "알수없음";
        Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?)");
        Matcher matcher = pattern.matcher(weightStr);
        if (matcher.find()) {
            try {
                double weight = Double.parseDouble(matcher.group(1));
                if (weight < 10) return "소형";
                if (weight < 25) return "중형";
                return "대형";
            } catch (NumberFormatException e) {
                return "알수없음";
            }
        }
        return "알수없음";
    }
}