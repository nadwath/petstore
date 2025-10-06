package com.caceis.petstore.service.impl;

import com.caceis.petstore.domain.Pet;
import com.caceis.petstore.repo.PetRepo;
import com.caceis.petstore.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {
    private final PetRepo repo;

    @Override
    public List<Pet> list() {
        return repo.findAll();
    }

    @Override
    public Pet get(Long id) {
        return repo.findById(id).orElseThrow();
    }

    @Override
    public List<Pet> findByStatus(String status) {
        return repo.findByStatus(status);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "setup", allEntries = true)
    public Pet create(Pet p) {
        return repo.save(p);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "setup", allEntries = true)
    public Pet update(Long id, Pet patch) {
        Pet p = get(id);
        if (patch.getName() != null) p.setName(patch.getName());
        if (patch.getCategory() != null) p.setCategory(patch.getCategory());
        if (patch.getStatus() != null) p.setStatus(patch.getStatus());
        if (patch.getTags() != null) p.setTags(patch.getTags());
        return repo.save(p);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "setup", allEntries = true)
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
