package org.challenge.calculator.model;

public class AppRecord {
    private String uuid;
    private String serviceName;
    private String serviceType;
    private String username;
    private long cost;
    private long balance;
    private String response;
    private long date;

    public AppRecord(String uuid, String serviceName, String serviceType,
                     String userName, long executionCost, long remainingBalance,
                     String response, long executionDate) {
        this.uuid = uuid;
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.username = userName;
        this.cost = executionCost;
        this.balance = remainingBalance;
        this.response = response;
        this.date = executionDate;
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
}
