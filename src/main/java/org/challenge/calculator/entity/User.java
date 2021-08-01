package org.challenge.calculator.entity;

import org.challenge.calculator.enums.UserStatusName;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

//I had to use a different name for the table since USER is a reserved word in Postgres
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique=true, nullable=false)
    private String uuid;

    @Column(unique=true, nullable=false)
    private String username;

    @Column(nullable=false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name="user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private UserStatusName status;

    @Column(nullable=false)
    private long balance;

    public User() {
        uuid = UUID.randomUUID().toString();
    }

    public User(String username, String password, Set<Role> roles, UserStatusName status, long balance) {
        super();
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.status = status;
        this.balance = balance;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public UserStatusName getStatus() {
        return status;
    }

    public void setStatus(UserStatusName status) {
        this.status = status;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }
}
