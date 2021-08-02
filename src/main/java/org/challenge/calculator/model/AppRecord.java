package org.challenge.calculator.model;

public class AppRecord {
    private String uuid;
    private String serviceName;
    private String userName;
    private long executionCost;
    private long remainingBalance;
    private String response;
    private long executionDate;

    public AppRecord(String uuid, String serviceName, String userName, long executionCost, long remainingBalance, String response, long executionDate) {
        this.uuid = uuid;
        this.serviceName = serviceName;
        this.userName = userName;
        this.executionCost = executionCost;
        this.remainingBalance = remainingBalance;
        this.response = response;
        this.executionDate = executionDate;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getExecutionCost() {
        return executionCost;
    }

    public void setExecutionCost(long executionCost) {
        this.executionCost = executionCost;
    }

    public long getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(long remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public long getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(long executionDate) {
        this.executionDate = executionDate;
    }
}
