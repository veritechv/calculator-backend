package org.challenge.calculator.entity;


import org.challenge.calculator.enums.RoleName;

import javax.persistence.*;

/**
 * This class represents the type of user that can use the application.
 * It serves to distinguish the operations that a user is allowed to do.
 */
@Entity
public class Role {

    /** ID in the database*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /** Role name, either ADMIN or USER*/
    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private RoleName roleName;

    /** Description of this role*/
    @Column
    private String description;

    public Role() {
    }

    public Role(RoleName roleName, String description) {
        this.roleName = roleName;
        this.description = description;
    }

    public RoleName getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleName roleName) {
        this.roleName = roleName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
