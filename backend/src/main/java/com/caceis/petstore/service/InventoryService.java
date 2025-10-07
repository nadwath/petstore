package com.caceis.petstore.service;

import com.caceis.petstore.domain.Inventory;

public interface InventoryService {
    Inventory getByPetId(Long petId);
    Inventory createInventory(Long petId, Integer initialQuantity);
    void addStock(Long petId, Integer quantity);
    void reserveStock(Long petId, Integer quantity);
    void releaseStock(Long petId, Integer quantity);
    void confirmStock(Long petId, Integer quantity);
}
