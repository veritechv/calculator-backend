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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("users")
public class UsersController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);
    private UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/listUsers")
    public Page<AppUser> listUsersPageable(Pageable pageable){
         return AppUserFactory.buildFromPageUser(userService.listUsers(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<User> searchUser(@RequestParam String username) {
        Optional<User> userOptional = userService.searchUser(username);
        if (userOptional.isEmpty()) {
            LOGGER.info("User [" + username + "] NOT FOUND");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
        }
    }

    @GetMapping("/search2")
    public ResponseEntity<User> searchUser(@RequestParam int userId) {
        Optional<User> userOptional = userService.searchUser(userId);
        if (userOptional.isEmpty()) {
            LOGGER.info("User with ID[" + userId + "] NOT FOUND");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
        }
    }


}
