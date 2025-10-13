package com.example.dyeTrack.core.valueobject;

import java.time.LocalDate;

import com.example.dyeTrack.core.entity.NutritionTrack;
import com.example.dyeTrack.core.entity.PhysioTrack;

import jakarta.persistence.Embeddable;

@Embeddable
public class DayDataOfUserVO {

    private final LocalDate dayData;
    private final PhysioTrack physioTrack;
    private final NutritionTrack nutritionTrack;
    private final SeanceTrackVO seanceTrack;

    public DayDataOfUserVO(LocalDate dayData, PhysioTrack physioTrack, NutritionTrack nutritionTrack,
            SeanceTrackVO seanceTrack) {
        this.dayData = dayData;
        this.physioTrack = physioTrack;
        this.nutritionTrack = nutritionTrack;
        this.seanceTrack = seanceTrack;
    }

    public LocalDate getDayData() {
        return dayData;
    }

    public PhysioTrack getPhysioTrack() {
        return physioTrack;
    }

    public NutritionTrack getNutritionTrack() {
        return nutritionTrack;
    }

    public SeanceTrackVO getSeanceTrack() {
        return seanceTrack;
    }

}
