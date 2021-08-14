package org.challenge.calculator.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class ServiceRequest {

    @Schema(description = "Service's uuid we want to execute", required = true)
    private String serviceUuid;

    @Schema(description = "User's username asking requesting the service", required = true)
    private String username;

    @Schema(description = "List of input parameters given by the user" )
    private List<String> parameters;

    public ServiceRequest(String serviceUUID, String username, List<String> parameters) {
        this.serviceUuid = serviceUUID;
        this.username = username;
        this.parameters = parameters;
    }

    public String getServiceUuid() {
        return serviceUuid;
    }

    public void setServiceUuid(String serviceUuid) {
        this.serviceUuid = serviceUuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }
}
