package org.challenge.calculator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.challenge.calculator.entity.User;
import org.challenge.calculator.model.Token;
import org.challenge.calculator.model.UserCredentials;
import org.challenge.calculator.services.LoginService;
import org.challenge.calculator.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/login")
@CrossOrigin
public class LoginController {
    private LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @Operation(summary = "Checks if the username & password combination is valid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "If the validation is successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Token.class))}),
            @ApiResponse(responseCode = "404",
                    description = "If the username is registered in the application",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400",
                    description = "If no username or password were provided",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "401",
                    description = "If the username/password is incorrect",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})})
    @PostMapping(value = "/authenticate")
    public ResponseEntity<Token> login(@RequestBody UserCredentials userCredentials) {
        ResponseEntity<Token> responseEntity;
        Token token = null;

        if (userCredentials != null &&
                StringUtils.isNoneBlank(userCredentials.getUsername(), userCredentials.getPassword())) {
            //TODO sanitize input
            String username = userCredentials.getUsername();
            String password = userCredentials.getPassword();
            token = loginService.loginUser(username, password);
        }

        responseEntity = token == null ?
                new ResponseEntity(JsonUtil.buildJsonSimpleResponse("Wrong username/password. Please verify"), HttpStatus.UNAUTHORIZED) :
                new ResponseEntity<>(token, HttpStatus.OK);

        return responseEntity;
    }

    @Operation(summary = "Registers a new user of the application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "If the validation is successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "409",
                    description = "If the username is already in use by another User",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400",
                    description = "If the username and/or password are not valid, eg: empty",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})})
    @PostMapping(value = "/signup")
    public ResponseEntity<String> register(@RequestBody UserCredentials userCredentials) {
        ResponseEntity<String> response = null;
        if (userCredentials != null &&
                StringUtils.isNoneBlank(userCredentials.getUsername(), userCredentials.getPassword())) {
            //TODO sanitize input
            String username = userCredentials.getUsername();
            String password = userCredentials.getPassword();

            User newUser = loginService.registerUser(username, password);
            if (newUser != null) {
                response = new ResponseEntity<>(JsonUtil.buildJsonSimpleResponse("User registration successful"), HttpStatus.OK);
            }
        }

        response = response == null ?
                new ResponseEntity<>(JsonUtil.buildJsonSimpleResponse("Username/Password incorrect"), HttpStatus.BAD_REQUEST) :
                response;

        return response;
    }
}
