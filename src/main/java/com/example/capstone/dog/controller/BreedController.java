package com.example.capstone.dog.controller;

import com.example.capstone.dog.dto.BreedDto;
import com.example.capstone.dog.service.BreedService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "breed", description = "견종 API")
@RequiredArgsConstructor
public class BreedController {

    private final BreedService breedService;

    @GetMapping("/breeds")
    public ResponseEntity<List<BreedDto>> getAllBreeds() {
        List<BreedDto> breeds = breedService.getAllBreeds();
        return ResponseEntity.ok(breeds);
    }
}