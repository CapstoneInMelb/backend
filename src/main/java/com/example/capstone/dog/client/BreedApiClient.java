package com.example.capstone.dog.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class BreedApiClient {

    @Value("${openapi.key}")
    private String serviceKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://apis.data.go.kr/1543061/abandonmentPublicService_v2")
            .build();

    public String fetchAllBreeds() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/kind_v2")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("up_kind_cd", "417000") // 417000: 개
                        .queryParam("_type", "json")
                        .queryParam("numOfRows", "1000")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}