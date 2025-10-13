
package com.example.dyeTrack.core.entity;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class SeanceTrack {
    @Id
    private Long id;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "day_data_id", unique = true)
    @JsonIgnore
    private DayDataOfUser dataOfUser;

    private LocalTime startHour;

    @ManyToOne
    @JoinColumn(name = "idTakenPartPreset", nullable = true)
    private PresetSeance presetSeance;

    @OneToMany(mappedBy = "seanceTrack", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlannedExercise> plannedExercises = new ArrayList<>();

    public SeanceTrack() {
    }

    public SeanceTrack(LocalTime startHour, PresetSeance presetSeance, DayDataOfUser dataOfUser) {
        this.startHour = startHour;
        this.presetSeance = presetSeance;
        this.dataOfUser = dataOfUser;
    }

    // --- Getters & Setters ---

    public DayDataOfUser getDataOfUser() {
        return dataOfUser;
    }

    public void setDataOfUser(DayDataOfUser dataOfUser) {
        this.dataOfUser = dataOfUser;
    }

    public LocalTime getStartHour() {
        return startHour;
    }

    public void setStartHour(LocalTime startHour) {
        this.startHour = startHour;
    }

    public PresetSeance getPresetSeance() {
        return presetSeance;
    }

    public void setPresetSeance(PresetSeance presetSeance) {
        this.presetSeance = presetSeance;
    }

    public List<PlannedExercise> getPlannedExercises() {
        return plannedExercises;
    }

    public void setPlannedExercises(List<PlannedExercise> plannedExercises) {
        this.plannedExercises = plannedExercises;
    }

}