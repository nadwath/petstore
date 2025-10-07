package com.caceis.petstore.service.impl;

import com.caceis.petstore.domain.Tag;
import com.caceis.petstore.exception.ResourceNotFoundException;
import com.caceis.petstore.repo.TagRepo;
import com.caceis.petstore.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepo repo;

    @Override
    @Cacheable(cacheNames = "tags")
    public List<Tag> list() {
        return repo.findAll();
    }

    @Override
    @Cacheable(cacheNames = "tags", key = "#id")
    public Tag get(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "tags", allEntries = true)
    public Tag create(Tag tag) {
        return repo.save(tag);
    }

    @Override
    @Transactional
    @Caching(
            put = @CachePut(cacheNames = "tags", key = "#id"),
            evict = @CacheEvict(cacheNames = "tags", allEntries = true)
    )
    public Tag update(Long id, Tag patch) {
        Tag tag = get(id);
        if (patch.getName() != null) tag.setName(patch.getName());
        return repo.save(tag);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "tags", allEntries = true)
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
