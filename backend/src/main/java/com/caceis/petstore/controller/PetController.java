package com.caceis.petstore.controller;

import com.caceis.petstore.domain.Pet;
import com.caceis.petstore.dto.CreatePetDTO;
import com.caceis.petstore.dto.PetDTO;
import com.caceis.petstore.dto.UpdatePetDTO;
import com.caceis.petstore.service.PetService;
import com.caceis.petstore.util.ObjectMapperUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pet")
@RequiredArgsConstructor
public class PetController {
    private final PetService svc;

    @GetMapping
    public List<PetDTO> list() {
        return ObjectMapperUtils.mapList(svc.list(), PetDTO.class);
    }

    @GetMapping("/findByStatus")
    public List<PetDTO> byStatus(@RequestParam String status) {
        return ObjectMapperUtils.mapList(svc.findByStatus(status), PetDTO.class);
    }

    @GetMapping("/{petId}")
    public PetDTO get(@PathVariable Long petId) {
        return ObjectMapperUtils.mapObject(svc.get(petId), PetDTO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public PetDTO create(@Valid @RequestBody CreatePetDTO dto) {
        Pet pet = ObjectMapperUtils.mapObject(dto, Pet.class);
        return ObjectMapperUtils.mapObject(svc.create(pet), PetDTO.class);
    }

    @PutMapping("/{petId}")
    @PreAuthorize("hasRole('ADMIN')")
    public PetDTO update(@PathVariable Long petId, @Valid @RequestBody UpdatePetDTO dto) {
        Pet pet = ObjectMapperUtils.mapObject(dto, Pet.class);
        return ObjectMapperUtils.mapObject(svc.update(petId, pet), PetDTO.class);
    }

    @DeleteMapping("/{petId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long petId) {
        svc.delete(petId);
    }
}
