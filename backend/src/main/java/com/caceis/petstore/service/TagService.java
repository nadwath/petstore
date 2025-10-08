package com.caceis.petstore.service;

import com.caceis.petstore.domain.Tag;

import java.util.List;

public interface TagService {
    List<Tag> list();
    Tag get(Long id);
    Tag create(Tag tag);
    Tag update(Long id, Tag tag);
    void delete(Long id);
}
