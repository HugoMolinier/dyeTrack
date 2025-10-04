package com.example.dyeTrack.core.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.dyeTrack.core.entity.PresetSeanceExercice.PresetSeanceExercice;
import com.example.dyeTrack.core.entity.RelExerciseMuscle.RelExerciseMuscle;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class PresetSeance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPresetSeance;

    @NotBlank
    private String name;

    @ManyToOne
    @JoinColumn(name = "idCreator", nullable = false)
    private User user;    

    @OneToMany(mappedBy = "presetSeance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PresetSeanceExercice> presetSeanceExercices = new ArrayList<>();

    public List<PresetSeanceExercice> getPresetSeanceExercice() {
        return presetSeanceExercices;
    }
    public PresetSeance() {}

    public PresetSeance(String name, User user) {
        this.name = name;
        this.user = user;
    }


    public Long getIdPresetSeance() {
        return idPresetSeance;
    }

    public void setIdPresetSeance(Long idPresetSeance) {
        this.idPresetSeance = idPresetSeance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
}
