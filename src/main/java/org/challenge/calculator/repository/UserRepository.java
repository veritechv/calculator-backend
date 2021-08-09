package org.challenge.calculator.repository;

import org.challenge.calculator.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUuid(String uuid);
    boolean existsByUsername(String username);
}
