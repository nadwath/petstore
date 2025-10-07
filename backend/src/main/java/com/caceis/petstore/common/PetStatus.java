package com.caceis.petstore.common;

import java.util.Arrays;
import java.util.List;

public enum PetStatus {
    AVAILABLE, SOLD, PENDING;

    public static List<String> getAll() {
        return Arrays.stream(PetStatus.values()).map(Enum::name).toList();
    }
}
