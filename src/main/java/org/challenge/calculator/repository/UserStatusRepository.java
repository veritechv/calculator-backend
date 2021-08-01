package org.challenge.calculator.repository;

import org.challenge.calculator.entity.UserStatus;
import org.challenge.calculator.enums.UserStatusName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserStatusRepository extends JpaRepository<UserStatus, Integer> {
    Optional<UserStatus> findByStatusName(UserStatusName statusName);
}
