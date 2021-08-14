package org.challenge.calculator.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Represents the service execution done by a user.\n" +
        "Every time a user executes a service a new Record is saved by application.")
public class AppRecord {
    @Schema(description = "Record's uuid")
    private String uuid;

    @Schema(description = "Name of the service executed")
    private String serviceName;

    @Schema(description = "Type of service executed")
    private String serviceType;

    @Schema(description = "User's username that requested the service")
    private String username;

    @Schema(description = "Service execution cost")
    private long cost;

    @Schema(description = "User's balance after the service execution")
    private long balance;

    @Schema(description = "Service's execution result")
    private String response;

    @Schema(description = "Timestamp of the execution")
    private long date;

    @Schema(description="Record's status, usually is ACTIVE")
    private String status;

    public AppRecord(String uuid, String serviceName, String serviceType,
                     String userName, long executionCost, long remainingBalance,
                     String response, long executionDate, String status) {
        this.uuid = uuid;
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.username = userName;
        this.cost = executionCost;
        this.balance = remainingBalance;
        this.response = response;
        this.date = executionDate;
        this.status = status;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
