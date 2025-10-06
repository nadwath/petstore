package com.caceis.petstore.controller;

import com.caceis.petstore.domain.User;
import com.caceis.petstore.dto.*;
import com.caceis.petstore.service.impl.AuthServiceImpl;
import com.caceis.petstore.service.impl.UserServiceImpl;
import com.caceis.petstore.util.ObjectMapperUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    private final AuthServiceImpl authService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserDTO dto) {
        User user = ObjectMapperUtils.mapObject(dto, User.class);
        User created = userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ObjectMapperUtils.mapObject(created, UserDTO.class));
    }

    @PostMapping("/createWithArray")
    public ResponseEntity<List<UserDTO>> createUsersWithArray(@Valid @RequestBody List<CreateUserDTO> dtos) {
        List<User> users = ObjectMapperUtils.mapList(dtos, User.class);
        List<User> created = userService.createWithList(users);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ObjectMapperUtils.mapList(created, UserDTO.class));
    }

    @PostMapping("/createWithList")
    public ResponseEntity<List<UserDTO>> createUsersWithList(@Valid @RequestBody List<CreateUserDTO> dtos) {
        List<User> users = ObjectMapperUtils.mapList(dtos, User.class);
        List<User> created = userService.createWithList(users);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ObjectMapperUtils.mapList(created, UserDTO.class));
    }

    @GetMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestParam String username,
            @RequestParam String password) {
        try {
            AuthServiceImpl.Tokens tokens = authService.login(username, password);
            LoginResponseDTO response = LoginResponseDTO.builder()
                    .accessToken(tokens.accessToken())
                    .refreshToken(tokens.refreshToken())
                    .tokenType("Bearer")
                    .expiresIn(3600L)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout() {
        // Logout logic would typically invalidate token
        // For now, returning success
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        try {
            User user = userService.getByUsername(username);
            return ResponseEntity.ok(ObjectMapperUtils.mapObject(user, UserDTO.class));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable String username,
            @Valid @RequestBody UpdateUserDTO dto) {
        try {
            User patch = ObjectMapperUtils.mapObject(dto, User.class);
            User updated = userService.update(username, patch);
            return ResponseEntity.ok(ObjectMapperUtils.mapObject(updated, UserDTO.class));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        try {
            userService.delete(username);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
