package org.challenge.calculator.entity;

import org.challenge.calculator.enums.ServiceName;
import org.challenge.calculator.enums.ServiceStatusName;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique=true, nullable=false)
    private String uuid;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private ServiceName name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceStatusName status;

    @Column(columnDefinition = "numeric default 0")
    private long cost;


    public Service() {
        uuid = UUID.randomUUID().toString();
    }

    public Service(String uuid, ServiceName serviceName, ServiceStatusName serviceStatusName, long cost) {
        super();
        this.uuid = uuid;
        this.name = serviceName;
        this.status = serviceStatusName;
        this.cost = cost;
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

    public ServiceName getName() {
        return name;
    }

    public void setName(ServiceName serviceName) {
        this.name = serviceName;
    }

    public ServiceStatusName getStatus() {
        return status;
    }

    public void setStatus(ServiceStatusName serviceStatusName) {
        this.status = serviceStatusName;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }
}
