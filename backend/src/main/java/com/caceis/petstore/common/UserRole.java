package com.caceis.petstore.common;

import java.util.Arrays;
import java.util.List;

public enum UserRole {
    ADMIN, USER;

    public static List<String> getAll() {
        return Arrays.stream(UserRole.values()).map(Enum::name).toList();
    }
}
