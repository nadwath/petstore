package com.caceis.petstore.service.impl;

import com.caceis.petstore.common.OrderStatus;
import com.caceis.petstore.domain.Order;
import com.caceis.petstore.exception.PetSoldOutException;
import com.caceis.petstore.exception.PetStoreRequestException;
import com.caceis.petstore.exception.ResourceNotFoundException;
import com.caceis.petstore.repo.OrderRepo;
import com.caceis.petstore.service.InventoryService;
import com.caceis.petstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepo repo;
    private final InventoryService inventoryService;

    @Override
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
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Order placeOrder(Order order) {
        // Validate order quantity
        if (order.getQuantity() == null || order.getQuantity() < 1) {
            throw new IllegalArgumentException("Order quantity must be at least 1");
        }

        // Reserve stock from inventory (with pessimistic lock to prevent race conditions)
        inventoryService.reserveStock(order.getPet().getId(), order.getQuantity());

        // Set order status to PLACED
        order.setStatus(OrderStatus.PLACED);
        order.setComplete(false);

        // Save and return the order
        return repo.save(order);
    }

    @Override
    public Order getOrder(Long orderId) {
        return repo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    @Override
    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = repo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Can only cancel orders that are not yet approved
        if (order.getStatus() == OrderStatus.APPROVED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new PetStoreRequestException("Cannot cancel order that is already approved or delivered");
        }

        // Release reserved stock back to inventory
        inventoryService.releaseStock(order.getPet().getId(), order.getQuantity());

        // Delete the order
        repo.deleteById(orderId);

        return order;
    }

    @Override
    @Transactional
    public Order approveOrder(Long orderId) {
        Order order = repo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Can only approve PLACED orders
        if (order.getStatus() != OrderStatus.PLACED) {
            throw new PetStoreRequestException("Only PLACED orders can be approved");
        }

        // Confirm the reserved stock (decrease actual quantity)
        inventoryService.confirmStock(order.getPet().getId(), order.getQuantity());

        // Update order status to APPROVED
        order.setStatus(OrderStatus.APPROVED);
        order.setComplete(true);

        return repo.save(order);
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        if (!repo.existsById(orderId)) {
            throw new ResourceNotFoundException("Order not found");
        }
        repo.deleteById(orderId);
    }
}
