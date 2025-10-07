package com.caceis.petstore.common;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    PLACED, APPROVED, DELIVERED;

    public static List<String> getAll() {
        return Arrays.stream(OrderStatus.values()).map(Enum::name).toList();
    }
}
