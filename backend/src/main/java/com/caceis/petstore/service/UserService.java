package com.caceis.petstore.service;

import com.caceis.petstore.domain.User;

import java.util.List;

public interface UserService {
    List<User> list();
    User getByUsername(String username);
    User create(User user);
    List<User> createWithList(List<User> users);
    User update(String username, User patch);
    void delete(String username);
}
