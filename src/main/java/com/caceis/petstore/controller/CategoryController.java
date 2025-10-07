package com.caceis.petstore.controller;

import com.caceis.petstore.domain.Category;
import com.caceis.petstore.dto.CategoryDTO;
import com.caceis.petstore.service.CategoryService;
import com.caceis.petstore.util.ObjectMapperUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDTO> list() {
        return ObjectMapperUtils.mapList(categoryService.list(), CategoryDTO.class);
    }

    @GetMapping("/{id}")
    public CategoryDTO get(@PathVariable Long id) {
        return ObjectMapperUtils.mapObject(categoryService.get(id), CategoryDTO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDTO create(@Valid @RequestBody CategoryDTO dto) {
        Category category = ObjectMapperUtils.mapObject(dto, Category.class);
        return ObjectMapperUtils.mapObject(categoryService.create(category), CategoryDTO.class);
    }

    @PutMapping("/{id}")
    public CategoryDTO update(@PathVariable Long id, @Valid @RequestBody CategoryDTO dto) {
        Category category = ObjectMapperUtils.mapObject(dto, Category.class);
        return ObjectMapperUtils.mapObject(categoryService.update(id, category), CategoryDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }
}
