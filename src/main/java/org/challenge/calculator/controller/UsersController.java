package org.challenge.calculator.controller;

import org.apache.commons.lang3.StringUtils;
import org.challenge.calculator.entity.User;
import org.challenge.calculator.model.AppUser;
import org.challenge.calculator.model.AppUserFactory;
import org.challenge.calculator.services.UserService;
import org.challenge.calculator.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller in charge of CRUD operations for User objects
 */
@RestController
@RequestMapping("/v1/users")
@CrossOrigin
public class UsersController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);
    private UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves a list of users using the paging information that come
     * in the parameters.
     *
     * @param pageIndex zero based index that indicates the page we want to retrieve
     * @param pageSize  number of elements included in each page
     * @param sortBy    field name used to sort the returne elements
     * @return A list of users along with paging information that can be used for the next call
     */
    @GetMapping
    public Page<AppUser> listUsers(@RequestParam(defaultValue = "0") Integer pageIndex,
                                   @RequestParam(defaultValue = "20") Integer pageSize,
                                   @RequestParam(defaultValue = "username") String sortBy) {
        return AppUserFactory.buildFromPageUser(userService.listUsers(pageIndex, pageSize, sortBy));
    }

    /**
     * Retrieves the user information that corresponds to the specified username
     *
     * @return the user information or an error if the username can't be found.
     */
    @GetMapping(value = "/{username}")
    public ResponseEntity<AppUser> searchUser(@PathVariable String username) {
        Optional<User> userOptional = userService.searchUser(username);
        if (!userOptional.isPresent()) {
            LOGGER.info("User [" + username + "] NOT FOUND");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(AppUserFactory.buildFromUser(userOptional.get()), HttpStatus.OK);
        }
    }

    /**
     * This endpoint handles User updates.
     * One thing we can't update is the password. For this we should use other
     * mechanism like "Forgot my password"
     *
     * @param appUser Object holding the information that should be updated.
     * @return The user, as ack, with the information updated.
     */
    @PutMapping
    public ResponseEntity<AppUser> updateUser(@RequestBody AppUser appUser) {
        ResponseEntity<AppUser> response;

        User updatedUser = userService.updateUser(AppUserFactory.buildFromAppUser(appUser));
        if (updatedUser != null) {
            response = new ResponseEntity<>(AppUserFactory.buildFromUser(updatedUser), HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "username") String username) {
        ResponseEntity<String> response;
        if (StringUtils.isNotBlank(username)) {
            userService.deleteUser(username);
            response = new ResponseEntity<>(JsonUtil.buildJsonSimpleResponse("User deleted successfully"), HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(JsonUtil.buildJsonSimpleResponse("The received information is wrong. Please verify"), HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    /**
     * Get the list/catalog of user roles
     */
    @GetMapping("/roles")
    public ResponseEntity<List<String>> userRoles() {
        return new ResponseEntity<>(userService.getUserRoles(), HttpStatus.OK);
    }

    /**
     * Get the list/catalog of user statuses
     */
    @GetMapping("/statuses")
    public ResponseEntity<List<String>> userStatuses() {
        return new ResponseEntity<>(userService.getUserStatuses(), HttpStatus.OK);
    }

}
