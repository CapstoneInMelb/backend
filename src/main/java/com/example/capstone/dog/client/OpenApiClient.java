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

    // 유기견 목록
    public String fetchRawJson(int page, String kindCd, String uprCd, String orgCd) {
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder
                            .path("/abandonmentPublic_v2")
                            .queryParam("serviceKey", serviceKey)
                            .queryParam("up_kind_cd", "417000") // 개
                            .queryParam("state", "notice")
                            .queryParam("pageNo", page)
                            .queryParam("numOfRows", 10)
                            .queryParam("_type", "json");

                    if (StringUtils.hasText(kindCd)) uriBuilder.queryParam("kind", kindCd);
                    if (StringUtils.hasText(uprCd))  uriBuilder.queryParam("upr_cd", uprCd);
                    if (StringUtils.hasText(orgCd))  uriBuilder.queryParam("org_cd", orgCd);

                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    // 시도 목록
    public String fetchSidoJson() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/sido_v2")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("numOfRows", 50)
                        .queryParam("_type", "json")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    // 시군구 목록 (해당 시도 코드 기준)
    public String fetchSigunguJson(String uprCd) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/sigungu_v2")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("upr_cd", uprCd)
                        .queryParam("numOfRows", 300)
                        .queryParam("_type", "json")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}