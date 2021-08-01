package org.challenge.calculator.entity;

import org.challenge.calculator.enums.UserStatusName;

import javax.persistence.*;

@Entity
public class UserStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(unique=true, nullable=false)
    private UserStatusName statusName;

    @Column
    private String description;

    public UserStatus() {
    }

    public UserStatus(UserStatusName statusName, String description) {
        this.statusName = statusName;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserStatusName getStatusName() {
        return statusName;
    }

    public void setStatusName(UserStatusName statusName) {
        this.statusName = statusName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
