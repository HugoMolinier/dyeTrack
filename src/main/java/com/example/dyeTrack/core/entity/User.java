package com.example.dyeTrack.core.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public User(Long id,String name){
        this.id =id;
        this.name=name;

    }
    public User(String name){
        this.name=name;
    }

    public User(){
    }

    public Long getId(){
        return id;
    }

    public String getName(){
        return name;
    }


    
}
