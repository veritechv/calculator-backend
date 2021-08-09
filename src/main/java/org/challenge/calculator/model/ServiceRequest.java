package org.challenge.calculator.model;

import java.util.List;

public class ServiceRequest {
    private String serviceUuid;
    private String username;
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
