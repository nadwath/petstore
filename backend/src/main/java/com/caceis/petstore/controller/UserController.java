package com.caceis.petstore.controller;

import com.caceis.petstore.domain.User;
import com.caceis.petstore.dto.*;
import com.caceis.petstore.service.AuthService;
import com.caceis.petstore.service.UserService;
import com.caceis.petstore.util.ObjectMapperUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@Valid @RequestBody CreateUserDTO dto) {
        User user = ObjectMapperUtils.mapObject(dto, User.class);
        User created = userService.create(user);
        return ObjectMapperUtils.mapObject(created, UserDTO.class);
    }

    @PostMapping("/createWithArray")
    @ResponseStatus(HttpStatus.CREATED)
    public List<UserDTO> createUsersWithArray(@Valid @RequestBody List<CreateUserDTO> dtos) {
        List<User> users = ObjectMapperUtils.mapList(dtos, User.class);
        List<User> created = userService.createWithList(users);
        return ObjectMapperUtils.mapList(created, UserDTO.class);
    }

    @PostMapping("/createWithList")
    @ResponseStatus(HttpStatus.CREATED)
    public List<UserDTO> createUsersWithList(@Valid @RequestBody List<CreateUserDTO> dtos) {
        List<User> users = ObjectMapperUtils.mapList(dtos, User.class);
        List<User> created = userService.createWithList(users);
        return ObjectMapperUtils.mapList(created, UserDTO.class);
    }

    @GetMapping("/{username}")
    public UserDTO getUserByUsername(@PathVariable String username) {
        User user = userService.getByUsername(username);
        return ObjectMapperUtils.mapObject(user, UserDTO.class);
    }

    @PutMapping("/{username}")
    public UserDTO updateUser(@PathVariable String username, @Valid @RequestBody UpdateUserDTO dto) {
        User patch = ObjectMapperUtils.mapObject(dto, User.class);
        User updated = userService.update(username, patch);
        return ObjectMapperUtils.mapObject(updated, UserDTO.class);
    }

    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String username) {
        userService.delete(username);
    }
}
