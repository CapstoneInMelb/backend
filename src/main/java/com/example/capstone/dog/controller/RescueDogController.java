package com.example.capstone.dog.controller;

import com.example.capstone.dog.dto.CodeNameDto;
import com.example.capstone.dog.dto.RescueDogDto;
import com.example.capstone.dog.service.RescueDogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rescue")
@Tag(name = "rescue", description = "유기견 API")
@RequiredArgsConstructor
public class RescueDogController {

    private final RescueDogService rescueDogService;

    // 드롭다운: 시도 목록
    @GetMapping("/regions/sido")
    public ResponseEntity<List<CodeNameDto>> getSido() {
        return ResponseEntity.ok(rescueDogService.getSidoList());
    }

    // 드롭다운: 시군구 목록 (선택된 시도 코드로 조회)
    @GetMapping("/regions/sigungu")
    public ResponseEntity<List<CodeNameDto>> getSigungu(@RequestParam String uprCd) {
        return ResponseEntity.ok(rescueDogService.getSigunguList(uprCd));
    }

    // 유기견 목록 (코드로 필터)
    @GetMapping("/dogs")
    public ResponseEntity<List<RescueDogDto>> getDogs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String kindCd,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String uprCd,   // 시도 코드
            @RequestParam(required = false) String orgCd    // 시군구 코드
    ) {
        List<RescueDogDto> dogs = rescueDogService.getDogs(page, kindCd, size, uprCd, orgCd);
        return ResponseEntity.ok(dogs);
    }
}