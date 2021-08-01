package org.challenge.calculator.services;

import org.challenge.calculator.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> searchUser(String username);
    Optional<User> searchUser(long userId);
    User saveUser(User user);
    User createUser(String username, String password);
    User updateUser(User user);
    List<User> listUsers();
    Page listUsers(Pageable pageableInformation);

}
