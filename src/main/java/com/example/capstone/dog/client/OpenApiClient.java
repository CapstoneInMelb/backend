package com.example.capstone.dog.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
@RequiredArgsConstructor
public class OpenApiClient {

    @Value("${openapi.key}")
    private String serviceKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://apis.data.go.kr/1543061/abandonmentPublicService_v2")
            .build();

    public String fetchRawJson(int page) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/abandonmentPublic_v2")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("bgnde", "20250101")
                        .queryParam("endde", "20250731")
                        .queryParam("pageNo", page)
                        .queryParam("numOfRows", 10)
                        .queryParam("_type", "json")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}