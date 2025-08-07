package com.example.capstone.dog.repository;

import com.example.capstone.dog.entity.Breed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BreedRepository extends JpaRepository<Breed, String> {
}