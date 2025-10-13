package com.example.dyeTrack.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;

@Entity
public class PhysioTrack {

    @Id
    private Long id;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "day_data_id", unique = true)
    @JsonIgnore
    private DayDataOfUser dataOfUser;

    private float weight;
    private int step;
    private float hourOfSleep;
    private String mood;

    public PhysioTrack() {
    }

    public PhysioTrack(float weight, int step, float hourOfSleep, String mood, DayDataOfUser dataOfUser) {
        this.weight = weight;
        this.step = step;
        this.hourOfSleep = hourOfSleep;
        this.mood = mood;
        this.dataOfUser = dataOfUser;
    }

    public PhysioTrack(float weight, int step, float hourOfSleep, String mood) {
        this.weight = weight;
        this.step = step;
        this.hourOfSleep = hourOfSleep;
        this.mood = mood;
    }

    // Getters

    public float getWeight() {
        return weight;
    }

    public int getStep() {
        return step;
    }

    public float getHourOfSleep() {
        return hourOfSleep;
    }

    public String getMood() {
        return mood;
    }

    public DayDataOfUser getDataOfUser() {
        return dataOfUser;
    }

    // Setters (sans setter pour l'id généré)
    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setHourOfSleep(float hourOfSleep) {
        this.hourOfSleep = hourOfSleep;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public void setDataOfUser(DayDataOfUser dataOfUser) {
        this.dataOfUser = dataOfUser;
    }
}
