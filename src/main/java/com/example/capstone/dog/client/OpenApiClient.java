package com.example.capstone.dog.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class OpenApiClient {

    @Value("${openapi.key}")
    private String serviceKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://apis.data.go.kr/1543061/abandonmentPublicService_v2")
            .build();

    public String fetchRawJson(int page, String kindCd) {
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder
                            .path("/abandonmentPublic_v2")
                            .queryParam("serviceKey", serviceKey)
                            .queryParam("up_kind_cd", "417000") // 개
                            .queryParam("state", "notice")     // 공고중인 동물만
                            .queryParam("pageNo", page)
                            .queryParam("numOfRows", 10)
                            .queryParam("_type", "json");

                    if (StringUtils.hasText(kindCd)) {
                        uriBuilder.queryParam("kind", kindCd);
                    }
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}