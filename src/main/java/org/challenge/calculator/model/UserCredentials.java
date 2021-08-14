package org.challenge.calculator.model;

import io.swagger.v3.oas.annotations.media.Schema;

/*
This class is meant to be used by the login and register endpoints
*/
@Schema(description = "Holds the username and password used for authentication")
public class UserCredentials {
    @Schema(description = "User's username that wants to use the application")
    private String username;

    @Schema(description = "User's passwword")
    private String password;


    public UserCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
