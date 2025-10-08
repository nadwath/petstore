package com.caceis.petstore.controller;

import com.caceis.petstore.domain.Tag;
import com.caceis.petstore.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tag")
@RequiredArgsConstructor
public class TagController {
    private final TagService service;

    @GetMapping
    public List<Tag> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public Tag get(@PathVariable Long id) {
        return service.get(id);
    }
}
