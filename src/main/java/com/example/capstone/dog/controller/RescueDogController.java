package com.example.capstone.dog.controller;

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

    @GetMapping("/dogs")
    public ResponseEntity<List<RescueDogDto>> getDogs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String kindCd,
            @RequestParam(required = false) String size
    ) {
        List<RescueDogDto> dogs = rescueDogService.getDogs(page, kindCd, size);
        return ResponseEntity.ok(dogs);
    }
}