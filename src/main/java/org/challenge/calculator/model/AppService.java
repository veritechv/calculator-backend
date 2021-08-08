package org.challenge.calculator.model;

public class AppService {
    private String uuid;
    private String name;
    private String description;
    private String status;
    private String type;
    private int numParameters;
    private long cost;

    public AppService(String uuid, String name, String description, String status, String type, int numParameters, long cost) {
        this.uuid = uuid;
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = type;
        this.numParameters = numParameters;
        this.cost = cost;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNumParameters() {
        return numParameters;
    }

    public void setNumParameters(int numParameters) {
        this.numParameters = numParameters;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }
}