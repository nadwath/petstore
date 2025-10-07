package com.caceis.petstore.controller;

import com.caceis.petstore.domain.Tag;
import com.caceis.petstore.dto.TagDTO;
import com.caceis.petstore.service.TagService;
import com.caceis.petstore.util.ObjectMapperUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tag")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class TagController {
    private final TagService tagService;

    @GetMapping
    public List<TagDTO> list() {
        return ObjectMapperUtils.mapList(tagService.list(), TagDTO.class);
    }

    @GetMapping("/{id}")
    public TagDTO get(@PathVariable Long id) {
        return ObjectMapperUtils.mapObject(tagService.get(id), TagDTO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDTO create(@Valid @RequestBody TagDTO dto) {
        Tag tag = ObjectMapperUtils.mapObject(dto, Tag.class);
        return ObjectMapperUtils.mapObject(tagService.create(tag), TagDTO.class);
    }

    @PutMapping("/{id}")
    public TagDTO update(@PathVariable Long id, @Valid @RequestBody TagDTO dto) {
        Tag tag = ObjectMapperUtils.mapObject(dto, Tag.class);
        return ObjectMapperUtils.mapObject(tagService.update(id, tag), TagDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        tagService.delete(id);
    }
}
