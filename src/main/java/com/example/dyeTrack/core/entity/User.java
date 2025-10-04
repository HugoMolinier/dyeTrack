package com.example.dyeTrack.core.entity;


import java.util.List;

import com.example.dyeTrack.core.entity.RelExerciseMuscle.RelExerciseMuscle;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "user")
    private List<PresetSeance> presets;

    public List<PresetSeance> getPreset() {
        return presets;
    }

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
