package org.challenge.calculator.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Represents the user of the calculator. Could it be an admin or a regular user.")
public class AppUser {
    @Schema(description = "User's uuid")
    private String uuid;

    @Schema(description = "User's username, eg:email address")
    private String username;

    @Schema(description = "User's list of roles, eg: SUPPORT")
    private List<String> roles;

    @Schema(description = "User's credits/points/chips she can spend on services")
    private long balance;

    @Schema(description = "User's status, eg: ACTIVE")
    private String status;

    public AppUser(String uuid, String username, List<String> roles, long balance, String status) {
        this.uuid = uuid;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
