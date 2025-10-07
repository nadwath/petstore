package com.caceis.petstore.controller;

import com.caceis.petstore.domain.Inventory;
import com.caceis.petstore.domain.Order;
import com.caceis.petstore.dto.CreateOrderDTO;
import com.caceis.petstore.dto.OrderDTO;
import com.caceis.petstore.service.InventoryService;
import com.caceis.petstore.service.OrderService;
import com.caceis.petstore.util.ObjectMapperUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
public class StoreController {
    private final OrderService orderService;
    private final InventoryService inventoryService;

    @GetMapping("/inventory")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Long> getInventory() {
        return orderService.getInventory();
    }

    @PostMapping("/order")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public OrderDTO placeOrder(@Valid @RequestBody CreateOrderDTO dto) {
        Order order = ObjectMapperUtils.mapObject(dto, Order.class);
        Order placed = orderService.placeOrder(order);
        return ObjectMapperUtils.mapObject(placed, OrderDTO.class);
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public OrderDTO getOrder(@PathVariable Long orderId) {
        Order order = orderService.getOrder(orderId);
        return ObjectMapperUtils.mapObject(order, OrderDTO.class);
    }

    @DeleteMapping("/order/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
    }

    @PostMapping("/order/{orderId}/cancel")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public OrderDTO cancelOrder(@PathVariable Long orderId) {
        Order order = orderService.cancelOrder(orderId);
        return ObjectMapperUtils.mapObject(order, OrderDTO.class);
    }

    @PostMapping("/order/{orderId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderDTO approveOrder(@PathVariable Long orderId) {
        Order order = orderService.approveOrder(orderId);
        return ObjectMapperUtils.mapObject(order, OrderDTO.class);
    }

    @PostMapping("/inventory/{petId}/add")
    @PreAuthorize("hasRole('ADMIN')")
    public Inventory addInventoryStock(@PathVariable Long petId, @RequestParam Integer quantity) {
        inventoryService.addStock(petId, quantity);
        return inventoryService.getByPetId(petId);
    }

    @GetMapping("/inventory/{petId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Inventory getInventoryByPetId(@PathVariable Long petId) {
        return inventoryService.getByPetId(petId);
    }
}
