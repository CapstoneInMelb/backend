package com.example.capstone.dog.controller;

import com.example.capstone.dog.dto.RescueDogDto;
import com.example.capstone.dog.service.RescueDogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rescue")
@RequiredArgsConstructor
public class RescueDogController {

    private final RescueDogService rescueDogService;

    @GetMapping("/dogs")
    public ResponseEntity<List<RescueDogDto>> getDogs(
            @RequestParam(defaultValue = "1") int page
    ) {
        List<RescueDogDto> dogs = rescueDogService.getDogs(page);
        return ResponseEntity.ok(dogs);
    }

    // 상세 조회는 프론트에서 처리하므로 삭제 또는 비활성화
    // @GetMapping("/dogs/{desertionNo}")
    // public ResponseEntity<RescueDogDto> getDogDetail(@PathVariable String desertionNo) {
    //     return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
    //         .body(null); // 또는 안내 메시지 반환
    // }

}