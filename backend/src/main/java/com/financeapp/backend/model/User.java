package com.financeapp.backend.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable=false)
    private String username;

    @Column(nullable=false)
    private String password;

    // no-arg ctor
    public User(){}

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    // getters/setters
    public Long getId(){ return id; }
    public String getUsername(){ return username; }
    public void setUsername(String username){ this.username = username; }
    public String getPassword(){ return password; }
    public void setPassword(String passwordHash){ this.password = passwordHash; }
}
