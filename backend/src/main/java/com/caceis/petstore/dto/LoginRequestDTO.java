package com.caceis.petstore.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @NotBlank
    private String username;
    
    @NotBlank
    private String encryptedPassword;
}