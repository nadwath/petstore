package com.caceis.petstore.repo;

import com.caceis.petstore.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {

}
