package org.challenge.calculator.entity;

import javax.persistence.*;

@Entity
public class SystemConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique=true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String value;

    @Column
    private String description;

    public SystemConfiguration() {
    }

    public SystemConfiguration(String name, String value, String description) {
        this.name = name;
        this.value = value;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
