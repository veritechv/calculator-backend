package org.challenge.calculator.controller;

import org.apache.commons.lang3.StringUtils;
import org.challenge.calculator.entity.User;
import org.challenge.calculator.exception.UserAlreadyExistsException;
import org.challenge.calculator.model.Token;
import org.challenge.calculator.model.UserCredentials;
import org.challenge.calculator.services.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@CrossOrigin
public class LoginController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    private LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping(value="/authenticate")
    public ResponseEntity<Token> login(@RequestBody UserCredentials userCredentials){
        ResponseEntity<Token> responseEntity;
        Token token = null;

        if(userCredentials!=null &&
                StringUtils.isNoneBlank(userCredentials.getUsername(), userCredentials.getPassword())) {
            //TODO sanitize input
            String username = userCredentials.getUsername();
            String password = userCredentials.getPassword();
            token = loginService.loginUser(username, password);
        }

        responseEntity = token == null ?
                new ResponseEntity<>(HttpStatus.UNAUTHORIZED) :
                new ResponseEntity<>(token, HttpStatus.OK);

        return responseEntity;
    }

    @PostMapping(value="/register")
    public ResponseEntity<String> register(@RequestBody UserCredentials userCredentials){
        ResponseEntity<String> response = null;
        if(userCredentials!=null &&
                StringUtils.isNoneBlank(userCredentials.getUsername(), userCredentials.getPassword())) {
            //TODO sanitize input
            String username = userCredentials.getUsername();
            String password = userCredentials.getPassword();
            try {
                User newUser = loginService.registerUser(username, password);
                if(newUser!=null){
                    response = new ResponseEntity<>(buildJsonSimpleResponse("User registration successful"), HttpStatus.OK);
                }
            }catch(UserAlreadyExistsException exception){
                response = new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
            }
        }

        response = response == null ?
                new ResponseEntity<>(buildJsonSimpleResponse("Username/Password incorrect"), HttpStatus.BAD_REQUEST) :
                response;

        return response;
    }

    private String buildJsonSimpleResponse(String response){
        return "{\"response\":\""+response+"\"}";
    }

}
