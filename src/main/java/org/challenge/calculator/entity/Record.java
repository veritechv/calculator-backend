package org.challenge.calculator.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique=true, nullable=false)
    private String uuid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_id", referencedColumnName = "id")
    private Service service;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private long cost;

    @Column(nullable = false)
    private long balance;

    @Column(nullable = false)
    private String response;

    @Column(nullable = false)
    private Date date;

    public Record() {
        uuid = UUID.randomUUID().toString();
    }

    public Record(Service serviceExecuted, User caller, long executionCost, long remainingBalance, String serviceResponse, Date executionDate) {
        this();
        this.service = serviceExecuted;
        this.user = caller;
        this.cost = executionCost;
        this.balance = remainingBalance;
        this.response = serviceResponse;
        this.date = executionDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service serviceExecuted) {
        this.service = serviceExecuted;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User caller) {
        this.user = caller;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long executionCost) {
        this.cost = executionCost;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long remainingBalance) {
        this.balance = remainingBalance;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String serviceResponse) {
        this.response = serviceResponse;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date executionDate) {
        this.date = executionDate;
    }
}
