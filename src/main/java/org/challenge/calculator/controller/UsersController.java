package org.challenge.calculator.controller;

import org.challenge.calculator.entity.User;
import org.challenge.calculator.services.UserService;
import org.challenge.calculator.model.AppUser;
import org.challenge.calculator.model.AppUserFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping
@CrossOrigin
public class UsersController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);
    private UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public Page<AppUser> listUsersPageable(Pageable pageable){
        return AppUserFactory.buildFromPageUser(userService.listUsers(pageable));
    }

    @GetMapping(value="/users/{username}")
    public ResponseEntity<AppUser> searchUser(@PathVariable String username) {
        Optional<User> userOptional = userService.searchUser(username);
        if (userOptional.isEmpty()) {
            LOGGER.info("User [" + username + "] NOT FOUND");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(AppUserFactory.buildFromUser(userOptional.get()), HttpStatus.OK);
        }
    }

   /* @GetMapping("/search/{id}")
    public ResponseEntity<AppUser> searchUser(@PathVariable int userId) {
        Optional<User> userOptional = userService.searchUser(userId);
        if (userOptional.isEmpty()) {
            LOGGER.info("User with ID[" + userId + "] NOT FOUND");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(AppUserFactory.buildFromUser(userOptional.get()), HttpStatus.OK);
        }
    }*/
}
