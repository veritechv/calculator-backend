package org.challenge.calculator.services;

import org.challenge.calculator.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/*
 * The user service is in charge of User's life-cycle, mainly CRUD operations.
 */
public interface UserService {
    /**
     * Retrieves a user by it's username/email
     *
     * @param username value used to find a User
     * @return An Optional that could be empty if the user is not found.
     */
    Optional<User> searchUser(String username);

    /**
     * Creates a new User in the application.
     *
     * @param user the object holding the user's data we want to create.
     * @return The user just created.
     */
    User createUser(User user);

    /**
     * Creates a new User in the application
     *
     * @param username username/email used to register the user in the application.
     * @param password password of the new user
     * @return The user just created
     */
    User createUser(String username, String password);

    /**
     * Updates the user information
     *
     * @param user object holding the data we want to update
     * @return the user just updated.
     */
    User updateUser(User user);

    /**
     * Removes/deletes a user from the application
     *
     * @param username username of the user we want to delete.
     */
    void deleteUser(String username);

    /**
     * Gets a list of users
     *
     * @param pageIndex    Page number we want back
     * @param pageSize     Number of users in the response list
     * @param sortingField Name of the attribute to use for sorting
     * @return
     */
    Page listUsers(int pageIndex, int pageSize, String sortingField);

    /**
     * Returns the list of the different user roles in the application
     */
    List<String> getUserRoles();

    /**
     * Returns the list of the different user status in the application
     */
    List<String> getUserStatuses();

}
