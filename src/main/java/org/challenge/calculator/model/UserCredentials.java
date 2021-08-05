package org.challenge.calculator.model;

/*
This class is meant to be used by the login and register endpoints
*/
public class UserCredentials {
    private String username;
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
