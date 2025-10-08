package com.caceis.petstore.service;

import com.caceis.petstore.domain.Role;

import java.util.List;

public interface RoleService {
    List<Role> list();
    Role get(Long id);
    Role create(Role role);
    Role update(Long id, Role role);
    void delete(Long id);
}
