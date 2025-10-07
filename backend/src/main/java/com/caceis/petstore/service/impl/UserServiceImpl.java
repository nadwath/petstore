package com.caceis.petstore.service.impl;

import com.caceis.petstore.domain.User;
import com.caceis.petstore.exception.ResourceNotFoundException;
import com.caceis.petstore.repo.UserRepo;
import com.caceis.petstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo repo;
    private final BCryptPasswordEncoder encoder;

    public List<User> list() {
        return repo.findAll();
    }

    @Override
    public User getByUsername(String username) {
        return repo.findByUsername(username).orElseThrow(() ->
                new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "setup", allEntries = true)
    public User create(User user) {
        if (repo.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "setup", allEntries = true)
    public List<User> createWithList(List<User> users) {
        for (User user : users) {
            user.setPassword(encoder.encode(user.getPassword()));
        }
        return repo.saveAll(users);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "setup", allEntries = true)
    public User update(String username, User patch) {
        User user = getByUsername(username);

        if (patch.getFirstName() != null)
            user.setFirstName(patch.getFirstName());
        if (patch.getLastName() != null)
            user.setLastName(patch.getLastName());
        if (patch.getEmail() != null)
            user.setEmail(patch.getEmail());
        if (patch.getPhone() != null)
            user.setPhone(patch.getPhone());
        if (patch.getUserStatus() != null)
            user.setUserStatus(patch.getUserStatus());
        if (patch.getRoles() != null)
            user.setRoles(patch.getRoles());
        if (patch.getRights() != null)
            user.setRights(patch.getRights());

        return repo.save(user);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "setup", allEntries = true)
    public void delete(String username) {
        User user = getByUsername(username);
        repo.delete(user);
    }
}
