package com.caceis.petstore.service.impl;

import com.caceis.petstore.domain.Category;
import com.caceis.petstore.exception.ResourceNotFoundException;
import com.caceis.petstore.repo.CategoryRepo;
import com.caceis.petstore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepo repo;

    @Override
    public List<Category> list() {
        return repo.findAll();
    }

    @Override
    public Category get(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    @Override
    @Transactional
    public Category create(Category category) {
        return repo.save(category);
    }

    @Override
    @Transactional
    public Category update(Long id, Category patch) {
        Category category = get(id);
        if (patch.getName() != null) category.setName(patch.getName());
        return repo.save(category);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
