package com.caceis.petstore.service;

import com.caceis.petstore.domain.Category;

import java.util.List;

public interface CategoryService {
    List<Category> list();
    Category get(Long id);
    Category create(Category category);
    Category update(Long id, Category category);
    void delete(Long id);
}
