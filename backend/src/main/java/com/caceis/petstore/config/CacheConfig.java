package com.caceis.petstore.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    // Cache names constants
    public static final String CACHE_PETS = "pets";
    public static final String CACHE_PETS_BY_STATUS = "petsByStatus";
    public static final String CACHE_CATEGORIES = "categories";
    public static final String CACHE_TAGS = "tags";
    public static final String CACHE_ROLES = "roles";
    public static final String CACHE_ORDERS = "orders";
    public static final String CACHE_ORDERS_BY_STATUS = "ordersByStatus";
}
