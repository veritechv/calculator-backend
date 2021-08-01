package org.challenge.calculator.repository;

import org.challenge.calculator.entity.Role;
import org.challenge.calculator.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findByRoleName(RoleName roleName);
}
