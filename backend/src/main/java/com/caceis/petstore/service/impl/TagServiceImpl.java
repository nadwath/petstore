package com.caceis.petstore.service.impl;

import com.caceis.petstore.domain.Tag;
import com.caceis.petstore.exception.ResourceNotFoundException;
import com.caceis.petstore.repo.TagRepo;
import com.caceis.petstore.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepo repo;

    @Override
    public List<Tag> list() {
        return repo.findAll();
    }

    @Override
    public Tag get(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
    }

    @Override
    @Transactional
    public Tag create(Tag tag) {
        return repo.save(tag);
    }

    @Override
    @Transactional
    public Tag update(Long id, Tag patch) {
        Tag tag = get(id);
        if (patch.getName() != null) tag.setName(patch.getName());
        return repo.save(tag);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
