package org.challenge.calculator.controller;

import org.challenge.calculator.entity.User;
import org.challenge.calculator.exception.UserAlreadyExistsException;
import org.challenge.calculator.services.LoginService;
import org.challenge.calculator.webmodel.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    private LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping(value="/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Token> login(@RequestBody MultiValueMap<String, String> formData){
        ResponseEntity<Token> responseEntity;
        Token token = null;

        if(formData!=null && !formData.isEmpty()) {
            String userName = formData.getFirst("username");
            String password = formData.getFirst("password");
            token = loginService.loginUser(userName, password);
        }

        responseEntity = token == null ?
                new ResponseEntity<>(HttpStatus.UNAUTHORIZED) :
                new ResponseEntity<>(token, HttpStatus.OK);

        return responseEntity;
    }

    @PostMapping(value="/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> register(@RequestBody MultiValueMap<String, String> formData){
        ResponseEntity<String> response = null;
        if(formData!=null && !formData.isEmpty()) {
            String username = formData.getFirst("username");
            String password = formData.getFirst("password");
            try {
                User newUser = loginService.registerUser(username, password);
                if(newUser!=null){
                    response = new ResponseEntity<>("User registration successful", HttpStatus.OK);
                }
            }catch(UserAlreadyExistsException exception){
                response = new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
            }
        }

        response = response == null ?
                new ResponseEntity<>("Username/Password incorrect", HttpStatus.BAD_REQUEST) :
                response;

        return response;
    }

}
