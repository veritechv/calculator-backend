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
    private ServiceName serviceName;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private ServiceStatusName serviceStatusName;

    @Column(columnDefinition = "numeric default 0")
    private long cost;


    public Service() {
        uuid = UUID.randomUUID().toString();
    }

    public Service(String uuid, ServiceName serviceName, ServiceStatusName serviceStatusName, long cost) {
        super();
        this.uuid = uuid;
        this.serviceName = serviceName;
        this.serviceStatusName = serviceStatusName;
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

    public ServiceName getServiceName() {
        return serviceName;
    }

    public void setServiceName(ServiceName serviceName) {
        this.serviceName = serviceName;
    }

    public ServiceStatusName getServiceStatusName() {
        return serviceStatusName;
    }

    public void setServiceStatusName(ServiceStatusName serviceStatusName) {
        this.serviceStatusName = serviceStatusName;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }
}
