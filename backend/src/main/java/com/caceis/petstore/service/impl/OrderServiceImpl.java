package com.caceis.petstore.service.impl;

import com.caceis.petstore.domain.Order;
import com.caceis.petstore.exception.ResourceNotFoundException;
import com.caceis.petstore.repo.OrderRepo;
import com.caceis.petstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepo repo;

    @Override
    @Cacheable(cacheNames = "orderInventory")
    public Map<String, Long> getInventory() {
        var results = repo.getInventory();
        Map<String, Long> inventory = new HashMap<>();

        results.forEach(result -> {
            String status = result.get("status").toString();
            Long count = ((Number) result.get("count")).longValue();
            inventory.put(status, count);
        });

        return inventory;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"orders", "orderInventory"}, allEntries = true)
    public Order placeOrder(Order order) {
        return repo.save(order);
    }

    @Override
    @Cacheable(cacheNames = "orders", key = "#orderId")
    public Order getOrder(Long orderId) {
        return repo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"orders", "orderInventory"}, allEntries = true)
    public void deleteOrder(Long orderId) {
        if (!repo.existsById(orderId)) {
            throw new ResourceNotFoundException("Order not found");
        }
        repo.deleteById(orderId);
    }
}
