package org.challenge.calculator.model;

import io.swagger.v3.oas.annotations.media.Schema;

public class ServiceResponse {

    @Schema(description = "Service's uuid that was executed")
    private String serviceUUID;

    @Schema(description = "User's username that requested the service")
    private String username;

    @Schema(description = "Input parameters used by the service")
    private String usedParameters;

    @Schema(description = "Result of the execution")
    private String response;

    @Schema(description = "User's balance after the service execution")
    private long remainingBalance;

    @Schema(description = "Date and time of the service execution in millis")
    private long executionDate;

    public ServiceResponse(String serviceUUID, String username, String usedParameters, String response, long remainingBalance, long executionDate) {
        this.serviceUUID = serviceUUID;
        this.username = username;
        this.usedParameters = usedParameters;
        this.response = response;
        this.remainingBalance = remainingBalance;
        this.executionDate = executionDate;
    }

    public ServiceResponse() {
    }

    public String getServiceUUID() {
        return serviceUUID;
    }

    public void setServiceUUID(String serviceUUID) {
        this.serviceUUID = serviceUUID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsedParameters() {
        return usedParameters;
    }

    public void setUsedParameters(String usedParameters) {
        this.usedParameters = usedParameters;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public long getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(long remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public long getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(long executionDate) {
        this.executionDate = executionDate;
    }
}
