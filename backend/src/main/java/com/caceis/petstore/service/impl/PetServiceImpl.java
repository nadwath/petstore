package com.caceis.petstore.service.impl;

import com.caceis.petstore.common.PetStatus;
import com.caceis.petstore.domain.Pet;
import com.caceis.petstore.repo.PetRepo;
import com.caceis.petstore.service.InventoryService;
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
    private final InventoryService inventoryService;

    @Override
    @Cacheable(cacheNames = "pets")
    public List<Pet> list() {
        return repo.findAll();
    }

    @Override
    @Cacheable(cacheNames = "pets", key = "#id")
    public Pet get(Long id) {
        return repo.findById(id).orElseThrow();
    }

    @Override
    @Cacheable(cacheNames = "petsByStatus", key = "#status")
    public List<Pet> findByStatus(String status) {
        return repo.findByStatus(status);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"pets", "petsByStatus", "inventory"}, allEntries = true)
    public Pet create(Pet p) {
        // Set pet status to AVAILABLE by default
        if (p.getStatus() == null) {
            p.setStatus(PetStatus.AVAILABLE);
        }

        // Save pet first
        Pet savedPet = repo.save(p);

        // Create inventory with initial stock of 1
        inventoryService.createInventory(savedPet.getId(), 1);

        return savedPet;
    }

    @Override
    @Transactional
    @Caching(
        put = @CachePut(cacheNames = "pets", key = "#id"),
        evict = {
            @CacheEvict(cacheNames = "pets", allEntries = true),
            @CacheEvict(cacheNames = "petsByStatus", allEntries = true)
        }
    )
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
    @CacheEvict(cacheNames = {"pets", "petsByStatus"}, allEntries = true)
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
