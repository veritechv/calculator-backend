package org.challenge.calculator.webmodel;

public class AppService {
    private long id;
    private String name;
    private String status;
    private long cost;


    public AppService(long id, String name, String status, long cost) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.cost = cost;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
