package com.caceis.petstore.controller;

import com.caceis.petstore.domain.Role;
import com.caceis.petstore.domain.User;
import com.caceis.petstore.dto.*;
import com.caceis.petstore.repo.RoleRepo;
import com.caceis.petstore.service.AuthService;
import com.caceis.petstore.service.UserService;
import com.caceis.petstore.util.ObjectMapperUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    private final RoleRepo roleRepo;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@Valid @RequestBody CreateUserDTO dto) {
        User user = ObjectMapperUtils.mapObject(dto, User.class);

        // Map role names to Role entities
        if (dto.getRoleIds() != null && !dto.getRoleIds().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            for (String roleName : dto.getRoleIds()) {
                roleRepo.findByName(roleName).ifPresent(roles::add);
            }
            user.setRoles(roles);
        }

        User created = userService.create(user);
        return ObjectMapperUtils.mapObject(created, UserDTO.class);
    }

    @PostMapping("/createWithArray")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> createUsersWithArray(@Valid @RequestBody List<CreateUserDTO> dtos) {
        List<User> users = ObjectMapperUtils.mapList(dtos, User.class);

        // Map role names to Role entities for each user
        for (int i = 0; i < dtos.size(); i++) {
            CreateUserDTO dto = dtos.get(i);
            if (dto.getRoleIds() != null && !dto.getRoleIds().isEmpty()) {
                Set<Role> roles = new HashSet<>();
                for (String roleName : dto.getRoleIds()) {
                    roleRepo.findByName(roleName).ifPresent(roles::add);
                }
                users.get(i).setRoles(roles);
            }
        }

        List<User> created = userService.createWithList(users);
        return ObjectMapperUtils.mapList(created, UserDTO.class);
    }

    @PostMapping("/createWithList")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> createUsersWithList(@Valid @RequestBody List<CreateUserDTO> dtos) {
        List<User> users = ObjectMapperUtils.mapList(dtos, User.class);

        // Map role names to Role entities for each user
        for (int i = 0; i < dtos.size(); i++) {
            CreateUserDTO dto = dtos.get(i);
            if (dto.getRoleIds() != null && !dto.getRoleIds().isEmpty()) {
                Set<Role> roles = new HashSet<>();
                for (String roleName : dto.getRoleIds()) {
                    roleRepo.findByName(roleName).ifPresent(roles::add);
                }
                users.get(i).setRoles(roles);
            }
        }

        List<User> created = userService.createWithList(users);
        return ObjectMapperUtils.mapList(created, UserDTO.class);
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO getUserByUsername(@PathVariable String username) {
        User user = userService.getByUsername(username);
        return ObjectMapperUtils.mapObject(user, UserDTO.class);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getAllUsers() {
        List<User> users = userService.getAll();
        return ObjectMapperUtils.mapList(users, UserDTO.class);
    }

    @PutMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO updateUser(@PathVariable String username, @Valid @RequestBody UpdateUserDTO dto) {
        User patch = ObjectMapperUtils.mapObject(dto, User.class);
        User updated = userService.update(username, patch);
        return ObjectMapperUtils.mapObject(updated, UserDTO.class);
    }

    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable String username) {
        userService.delete(username);
    }
}
