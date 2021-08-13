package org.challenge.calculator.entity;

import org.challenge.calculator.enums.ServiceStatus;
import org.challenge.calculator.enums.ServiceType;

import javax.persistence.*;
import java.util.UUID;

/**
 * This class represents an operation that the calculator can do.
 */
@Entity
public class Service {
    /** ID in the database*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** UUID  */
    @Column(unique=true, nullable=false)
    private String uuid;

    /** Service name, eg: addition, power, log*/
    @Column(unique=true, nullable = false)
    private String name;

    /** Description of what the service does*/
    @Column
    private String description;

    /** Number of parameters the service needs to work*/
    @Column(columnDefinition = "numeric default 1")
    private int numParameters;

    /** Defines the type or category of service */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceType type;

    /** Status, determines if a service can be executed or not*/
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceStatus status;

    /** How much it cost to use this service*/
    @Column(columnDefinition = "numeric default 0")
    private long cost;


    public Service() {
        uuid = UUID.randomUUID().toString();
    }

    public Service(String uuid, String name, String description, int numParameters, ServiceType type, ServiceStatus status, long cost) {
        this.uuid = uuid;
        this.name = name;
        this.description = description;
        this.numParameters = numParameters;
        this.type = type;
        this.status = status;
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

    public ServiceType getType() {
        return type;
    }

    public void setType(ServiceType type) {
        this.type = type;
    }

    public ServiceStatus getStatus() {
        return status;
    }

    public void setStatus(ServiceStatus status) {
        this.status = status;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public int getNumParameters() {
        return numParameters;
    }

    public void setNumParameters(int numParameters) {
        this.numParameters = numParameters;
    }

    public boolean isInactive(){
        return status != null && status == ServiceStatus.INACTIVE;
    }

    public boolean isDeleted(){
        return status != null && status == ServiceStatus.DELETED;
    }
}
