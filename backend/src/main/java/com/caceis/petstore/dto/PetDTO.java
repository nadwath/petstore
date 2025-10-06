package com.caceis.petstore.dto;

import com.caceis.petstore.common.PetStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetDTO {
    private Long id;
    private String name;
    private CategoryDTO category;
    private Set<String> photoUrls;
    private Set<TagDTO> tags;
    private PetStatus status;
}