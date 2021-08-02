package org.challenge.calculator.model;

public class AppService {
    private String uuid;
    private String name;
    private String status;
    private long cost;


    public AppService(String uuid, String name, String status, long cost) {
        this.uuid = uuid;
        this.name = name;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }
}
