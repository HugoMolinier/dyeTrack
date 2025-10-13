package com.example.dyeTrack.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;

@Entity
public class NutritionTrack {

    @Id
    private Long id;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "day_data_id", unique = true)
    @JsonIgnore
    private DayDataOfUser dataOfUser;

    private int calories;
    private int proteins;
    private int lipids;
    private int carbohydrates;
    private int fiber;
    private int cafeins;

    public NutritionTrack() {
    }

    public NutritionTrack(int calories, int proteins, int lipids, int carbohydrates, int fiber, int cafeins) {
        this.calories = calories;
        this.proteins = proteins;
        this.lipids = lipids;
        this.carbohydrates = carbohydrates;
        this.fiber = fiber;
        this.cafeins = cafeins;
    }

    public NutritionTrack(int calories, int proteins, int lipids, int carbohydrates, int fiber, int cafeins,
            DayDataOfUser dataOfUser) {
        this.calories = calories;
        this.proteins = proteins;
        this.lipids = lipids;
        this.carbohydrates = carbohydrates;
        this.fiber = fiber;
        this.cafeins = cafeins;
        this.dataOfUser = dataOfUser;
    }

    public int getCalories() {
        return calories;
    }

    public int getProteins() {
        return proteins;
    }

    public int getLipids() {
        return lipids;
    }

    public int getCarbohydrates() {
        return carbohydrates;
    }

    public int getFiber() {
        return fiber;
    }

    public int getCafeins() {
        return cafeins;
    }

    public DayDataOfUser getDataOfUser() {
        return dataOfUser;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setProteins(int proteins) {
        this.proteins = proteins;
    }

    public void setLipids(int lipids) {
        this.lipids = lipids;
    }

    public void setCarbohydrates(int carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public void setFiber(int fiber) {
        this.fiber = fiber;
    }

    public void setCafeins(int cafeins) {
        this.cafeins = cafeins;
    }

    public void setDataOfUser(DayDataOfUser dataOfUser) {
        this.dataOfUser = dataOfUser;
    }
}
