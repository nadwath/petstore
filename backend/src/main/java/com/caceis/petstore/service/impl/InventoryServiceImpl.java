package com.caceis.petstore.service.impl;

import com.caceis.petstore.domain.Inventory;
import com.caceis.petstore.domain.Pet;
import com.caceis.petstore.exception.PetSoldOutException;
import com.caceis.petstore.exception.ResourceNotFoundException;
import com.caceis.petstore.repo.InventoryRepo;
import com.caceis.petstore.repo.PetRepo;
import com.caceis.petstore.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepo inventoryRepo;
    private final PetRepo petRepo;

    @Override
    public Inventory getByPetId(Long petId) {
        return inventoryRepo.findByPetId(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for pet"));
    }

    @Override
    @Transactional
    public Inventory createInventory(Long petId, Integer initialQuantity) {
        Pet pet = petRepo.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));

        Inventory inventory = Inventory.builder()
                .pet(pet)
                .quantity(initialQuantity)
                .reserved(0)
                .build();

        return inventoryRepo.save(inventory);
    }

    @Override
    @Transactional
    public void addStock(Long petId, Integer quantity) {
        Inventory inventory = inventoryRepo.findByPetIdWithLock(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for pet"));

        inventory.addStock(quantity);
        inventoryRepo.save(inventory);
    }

    @Override
    @Transactional
    public void reserveStock(Long petId, Integer quantity) {
        Inventory inventory = inventoryRepo.findByPetIdWithLock(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for pet"));

        if (!inventory.hasAvailableStock(quantity)) {
            throw new PetSoldOutException("Insufficient stock available for pet");
        }

        inventory.reserveStock(quantity);
        inventoryRepo.save(inventory);
    }

    @Override
    @Transactional
    public void releaseStock(Long petId, Integer quantity) {
        Inventory inventory = inventoryRepo.findByPetIdWithLock(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for pet"));

        inventory.releaseReservedStock(quantity);
        inventoryRepo.save(inventory);
    }

    @Override
    @Transactional
    public void confirmStock(Long petId, Integer quantity) {
        Inventory inventory = inventoryRepo.findByPetIdWithLock(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for pet"));

        inventory.confirmReservedStock(quantity);
        inventoryRepo.save(inventory);
    }
}
