package com.caceis.petstore.service;

import com.caceis.petstore.domain.Pet;

import java.util.List;

public interface PetService {
    List<Pet> list();
    Pet get(Long id);
    List<Pet> findByStatus(String status);
    Pet create(Pet pet);
    Pet update(Long id, Pet patch);
    void delete(Long id);
}
