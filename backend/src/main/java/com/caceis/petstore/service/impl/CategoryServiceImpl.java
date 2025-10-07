package com.caceis.petstore.service.impl;

import com.caceis.petstore.domain.Category;
import com.caceis.petstore.exception.ResourceNotFoundException;
import com.caceis.petstore.repo.CategoryRepo;
import com.caceis.petstore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepo repo;

    @Override
    @Cacheable(cacheNames = "categories")
    public List<Category> list() {
        return repo.findAll();
    }

    @Override
    @Cacheable(cacheNames = "categories", key = "#id")
    public Category get(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "categories", allEntries = true)
    public Category create(Category category) {
        return repo.save(category);
    }

    @Override
    @Transactional
    @Caching(
            put = @CachePut(cacheNames = "categories", key = "#id"),
            evict = @CacheEvict(cacheNames = "categories", allEntries = true)
    )
    public Category update(Long id, Category patch) {
        Category category = get(id);
        if (patch.getName() != null) category.setName(patch.getName());
        return repo.save(category);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "categories", allEntries = true)
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
