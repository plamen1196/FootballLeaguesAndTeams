package com.example.exam2410.model.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    private String username;
    private String fullName;
    private String password;
    private boolean isActive;
    private Set<UserRole> roles = new HashSet<>();

    @Column(nullable = false)
    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    @Column(nullable = false, unique = true)
    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    @Column(nullable = false)
    public String getFullName() {
        return fullName;
    }

    public User setFullName(String firstName) {
        this.fullName = firstName;
        return this;
    }


    @Column
    public boolean isActive() {
        return isActive;
    }

    public User setActive(boolean active) {
        isActive = active;
        return this;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    public Set<UserRole> getRoles() {
        return roles;
    }

    public User setRoles(
            Set<UserRole> roles) {
        this.roles = roles;
        return this;
    }
}
