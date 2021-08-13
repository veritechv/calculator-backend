package org.challenge.calculator.entity;

import javax.persistence.*;

/**
 * This class represents an application-wide configuration.
 */
@Entity
public class SystemConfiguration {
    /** ID in the database*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /** Name of the configuration, eg: Default balance*/
    @Column(unique=true, nullable = false)
    private String name;

    /** Value of the configuration, eg: "0.0"*/
    @Column(nullable = false)
    private String value;

    /** What is this configuration for */
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
