package com.caceis.petstore.controller;

import com.caceis.petstore.dto.RoleDTO;
import com.caceis.petstore.service.RoleService;
import com.caceis.petstore.util.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<RoleDTO> list() {
        return ObjectMapperUtils.mapList(roleService.list(), RoleDTO.class);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RoleDTO get(@PathVariable Long id) {
        return ObjectMapperUtils.mapObject(roleService.get(id), RoleDTO.class);
    }
}
