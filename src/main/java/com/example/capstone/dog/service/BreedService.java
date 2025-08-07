package com.example.capstone.dog.service;

import com.example.capstone.dog.dto.BreedDto;
import com.example.capstone.dog.entity.Breed;
import com.example.capstone.dog.repository.BreedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BreedService {

    private final BreedRepository breedRepository;

    @Transactional(readOnly = true) // 읽기 전용 트랜잭션으로 성능 최적화
    public List<BreedDto> getAllBreeds() {
        return breedRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private BreedDto convertToDto(Breed breed) {
        BreedDto dto = new BreedDto();
        dto.setKindCd(breed.getKindCd());
        dto.setKindName(breed.getKindName());
        return dto;
    }
}