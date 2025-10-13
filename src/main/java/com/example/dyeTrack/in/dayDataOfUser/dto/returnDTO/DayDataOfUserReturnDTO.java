package com.example.dyeTrack.in.dayDataOfUser.dto.returnDTO;

import java.time.LocalDate;

import com.example.dyeTrack.core.entity.DayDataOfUser;
import com.example.dyeTrack.core.entity.NutritionTrack;
import com.example.dyeTrack.core.entity.PhysioTrack;

public class DayDataOfUserReturnDTO {

    private Long idDayData;
    private LocalDate dayData;
    private PhysioTrack physioTrack;
    private NutritionTrack nutritionTrack;
    private SeanceTrackReturnDTO seanceTrack;

    public DayDataOfUserReturnDTO() {
    }

    public DayDataOfUserReturnDTO(DayDataOfUser dataOfUser) {
        this.idDayData = dataOfUser.getIdDayData();
        this.dayData = dataOfUser.getDay();
        this.physioTrack = dataOfUser.getPhysioTrack();
        this.nutritionTrack = dataOfUser.getNutritionTrack();
        if (dataOfUser.getSeanceTrack() != null) {
            this.seanceTrack = new SeanceTrackReturnDTO(dataOfUser.getSeanceTrack());
        }
    }

    // Getters et Setters
    public Long getIdDayData() {
        return idDayData;
    }

    public void setIdDayData(Long idDayData) {
        this.idDayData = idDayData;
    }

    public LocalDate getDayData() {
        return dayData;
    }

    public void setDayData(LocalDate dayData) {
        this.dayData = dayData;
    }

    public PhysioTrack getPhysioTrack() {
        return physioTrack;
    }

    public void setPhysioTrack(PhysioTrack physioTrack) {
        this.physioTrack = physioTrack;
    }

    public NutritionTrack getNutritionTrack() {
        return nutritionTrack;
    }

    public void setNutritionTrack(NutritionTrack nutritionTrack) {
        this.nutritionTrack = nutritionTrack;
    }

    public SeanceTrackReturnDTO getSeanceTrack() {
        return seanceTrack;
    }

    public void setSeanceTrack(SeanceTrackReturnDTO seanceTrack) {
        this.seanceTrack = seanceTrack;
    }
}
