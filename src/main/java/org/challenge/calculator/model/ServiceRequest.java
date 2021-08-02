package org.challenge.calculator.model;

import java.util.List;

public class ServiceRequest {
    private String serviceUUID;
    private String username;
    private List<String> parameters;

    public ServiceRequest(String serviceUUID, String username, List<String> parameters) {
        this.serviceUUID = serviceUUID;
        this.username = username;
        this.parameters = parameters;
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

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }
}
