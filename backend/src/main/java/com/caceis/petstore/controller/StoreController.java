package com.caceis.petstore.controller;

import com.caceis.petstore.domain.Order;
import com.caceis.petstore.dto.CreateOrderDTO;
import com.caceis.petstore.dto.OrderDTO;
import com.caceis.petstore.service.OrderService;
import com.caceis.petstore.util.ObjectMapperUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
public class StoreController {
    private final OrderService orderService;

    @GetMapping("/inventory")
    public Map<String, Long> getInventory() {
        return orderService.getInventory();
    }

    @PostMapping("/order")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO placeOrder(@Valid @RequestBody CreateOrderDTO dto) {
        Order order = ObjectMapperUtils.mapObject(dto, Order.class);
        Order placed = orderService.placeOrder(order);
        return ObjectMapperUtils.mapObject(placed, OrderDTO.class);
    }

    @GetMapping("/order/{orderId}")
    public OrderDTO getOrder(@PathVariable Long orderId) {
        Order order = orderService.getOrder(orderId);
        return ObjectMapperUtils.mapObject(order, OrderDTO.class);
    }

    @DeleteMapping("/order/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
    }
}
