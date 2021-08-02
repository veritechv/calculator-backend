package org.challenge.calculator.model;

import java.util.List;

public class AppUser {
    private String username;
    private List<String> roles;
    private long balance;
    private String status;

    public AppUser(String username, List<String> roles, long balance, String status) {
        this.username = username;
        this.roles = roles;
        this.balance = balance;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
