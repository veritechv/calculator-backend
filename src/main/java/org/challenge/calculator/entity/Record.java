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

    @Column(nullable = false)
    private long executionCost;

    @Column(nullable = false)
    private long remainingBalance;

    @Column(nullable = false)
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
}
