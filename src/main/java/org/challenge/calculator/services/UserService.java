package org.challenge.calculator.services;

import org.challenge.calculator.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> searchUser(String username);
    User saveUser(User user);
    User createUser(String username, String password);
    User updateUser(User user);
    void deleteUser(String username);

    /*
     * Returns an specific page of users
     */
    Page listUsers(int pageIndex, int pageSize, String sortingField);

    /*
     * Returns the list of available roles as strings
     */
    List<String> getUserRoles();

    /*
     * Returns the list of available user status
     */
    List<String> getUserStatuses();

}
