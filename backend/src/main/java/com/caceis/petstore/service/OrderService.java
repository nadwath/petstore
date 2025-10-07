package com.caceis.petstore.service;

import com.caceis.petstore.domain.Order;

import java.util.Map;

public interface OrderService {
    Map<String, Long> getInventory();
    Order placeOrder(Order order);
    Order getOrder(Long orderId);
    void deleteOrder(Long orderId);
    Order cancelOrder(Long orderId);
    Order approveOrder(Long orderId);
}
