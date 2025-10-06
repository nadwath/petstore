package com.caceis.petstore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserDTO {
    @NotBlank
    private String username;
    private String firstName;
    private String lastName;
    @Email
    private String email;
    @NotBlank
    private String password;
    private String phone;
    private Integer userStatus;
    private Set<Long> roleIds;
}