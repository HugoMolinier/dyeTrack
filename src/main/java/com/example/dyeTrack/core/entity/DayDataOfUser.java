package com.example.dyeTrack.core.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "idUser", "dayData" }))
public class DayDataOfUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDayData;

    @Column(name = "day_data", nullable = false)
    private LocalDate dayData;

    @ManyToOne
    @JoinColumn(name = "idUser", nullable = true)
    @JsonIgnore
    private User user;

    @OneToOne(mappedBy = "dataOfUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private PhysioTrack physioTrack;

    public PhysioTrack getPhysioTrack() {
        return physioTrack;
    }

    public void setPhysioTrack(PhysioTrack physioTrack) {
        this.physioTrack = physioTrack;
    }

    @OneToOne(mappedBy = "dataOfUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private NutritionTrack nutritionTrack;

    public NutritionTrack getNutritionTrack() {
        return nutritionTrack;
    }

    public void setNutritionTrack(NutritionTrack nutritionTrack) {
        this.nutritionTrack = nutritionTrack;
    }

    @OneToOne(mappedBy = "dataOfUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private SeanceTrack seanceTrack;

    public SeanceTrack getSeanceTrack() {
        return seanceTrack;
    }

    public void setSeanceTrack(SeanceTrack seanceTrack) {
        this.seanceTrack = seanceTrack;
    }

    public DayDataOfUser() {
    }

    public DayDataOfUser(LocalDate dayData, User user) {
        this.dayData = dayData;
        this.user = user;
    }

    // Getters
    public Long getIdDayData() {
        return idDayData;
    }

    public LocalDate getDay() {
        return dayData;
    }

    public User getUser() {
        return user;
    }

    public void setDay(LocalDate dayData) {
        this.dayData = dayData;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
