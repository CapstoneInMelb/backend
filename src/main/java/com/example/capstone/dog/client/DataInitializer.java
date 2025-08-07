package com.example.capstone.dog.client;

import com.example.capstone.dog.dto.BreedDto;
import com.example.capstone.dog.entity.Breed;
import com.example.capstone.dog.repository.BreedRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final BreedApiClient breedApiClient;
    private final BreedRepository breedRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(ApplicationArguments args) {
        if (breedRepository.count() > 0) {
            log.info("DB에 이미 견종 데이터가 존재합니다.");
            return;
        }

        log.info("DB에 견종 데이터가 없어 API를 통해 초기화를 시도합니다.");
        try {
            String jsonResponse = breedApiClient.fetchAllBreeds();
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode itemsNode = root.path("response").path("body").path("items");

            // 'item'이 없을 경우를 대비한 방어 코드
            if (itemsNode.isMissingNode() || itemsNode.path("item").isMissingNode()) {
                log.warn("API 응답에 'items' 또는 'item' 데이터가 없습니다.");
                return;
            }

            JsonNode itemData = itemsNode.path("item");
            List<Breed> breedsToSave = new ArrayList<>();

            // 1. item이 배열인 경우 (결과가 여러 개)
            if (itemData.isArray()) {
                for (JsonNode itemNode : itemData) {
                    parseAndAddBreed(itemNode, breedsToSave);
                }
            }
            // 2. item이 객체인 경우 (결과가 1개)
            else if (itemData.isObject()) {
                parseAndAddBreed(itemData, breedsToSave);
            }

            if (!breedsToSave.isEmpty()) {
                breedRepository.saveAll(breedsToSave);
                log.info("총 {}개의 유효한 견종 데이터를 DB에 저장했습니다.", breedsToSave.size());
            }

        } catch (WebClientResponseException e) {
            log.error("공공데이터 API 호출에 실패했습니다. Status: {}, Response: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("품종 데이터 초기화 중 알 수 없는 오류가 발생했습니다.", e);
        }
    }

    /**
     * JsonNode를 파싱하여 Breed 리스트에 추가하는 헬퍼 메소드
     */
    private void parseAndAddBreed(JsonNode itemNode, List<Breed> breedList) {
        String kindCd = itemNode.path("kindCd").asText(null);

        // 1순위: kindNm, 2순위: knm 순서로 이름 필드를 찾습니다.
        String kindName = itemNode.path("kindNm").asText(null);
        if (kindName == null) {
            kindName = itemNode.path("knm").asText(null);
        }

        if (kindCd != null && kindName != null && !kindName.isBlank()) {
            breedList.add(new Breed(kindCd, kindName));
            log.info("추가된 데이터 -> 코드: [{}], 이름: [{}]", kindCd, kindName);
        } else {
            log.warn("유효하지 않은 데이터는 건너뜁니다 -> 코드: [{}], 이름: [{}]", kindCd, kindName);
        }
    }
}