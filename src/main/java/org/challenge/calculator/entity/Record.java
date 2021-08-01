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
    private Service serviceExecuted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User caller;

    @Column(nullable = false, name="cost")
    private long executionCost;

    @Column(nullable = false, name="balance")
    private long remainingBalance;

    @Column(nullable = false, name="response")
    private String serviceResponse;

    @Column(nullable = false)
    private Date executionDate;

    public Record() {
        uuid = UUID.randomUUID().toString();
    }

    public Record(Service serviceExecuted, User caller, long executionCost, long remainingBalance, String serviceResponse, Date executionDate) {
        this.serviceExecuted = serviceExecuted;
        this.caller = caller;
        this.executionCost = executionCost;
        this.remainingBalance = remainingBalance;
        this.serviceResponse = serviceResponse;
        this.executionDate = executionDate;
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

    public Service getServiceExecuted() {
        return serviceExecuted;
    }

    public void setServiceExecuted(Service serviceExecuted) {
        this.serviceExecuted = serviceExecuted;
    }

    public User getCaller() {
        return caller;
    }

    public void setCaller(User caller) {
        this.caller = caller;
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

    public String getServiceResponse() {
        return serviceResponse;
    }

    public void setServiceResponse(String serviceResponse) {
        this.serviceResponse = serviceResponse;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }
}
