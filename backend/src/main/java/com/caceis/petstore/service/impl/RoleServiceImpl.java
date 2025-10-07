package com.caceis.petstore.service.impl;

import com.caceis.petstore.domain.Role;
import com.caceis.petstore.exception.ResourceNotFoundException;
import com.caceis.petstore.repo.RoleRepo;
import com.caceis.petstore.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepo repo;

    @Override
    @Cacheable(cacheNames = "roles")
    public List<Role> list() {
        return repo.findAll();
    }

    @Override
    @Cacheable(cacheNames = "roles", key = "#id")
    public Role get(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "roles", allEntries = true)
    public Role create(Role role) {
        return repo.save(role);
    }

    @Override
    @Transactional
    @Caching(
            put = @CachePut(cacheNames = "roles", key = "#id"),
            evict = @CacheEvict(cacheNames = "roles", allEntries = true)
    )
    public Role update(Long id, Role patch) {
        Role role = get(id);
        if (patch.getName() != null) role.setName(patch.getName());
        return repo.save(role);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "roles", allEntries = true)
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
