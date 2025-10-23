package com.example.dyeTrack.core.valueobject;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SeanceTrackVO {

    private LocalTime startHour;
    private Long presetSeanceId;
    private List<PlannedExerciseVO> plannedExercises = new ArrayList<>(); // initialisation ici

    public SeanceTrackVO() {
    }

    public SeanceTrackVO(LocalTime startHour, Long presetSeanceId, List<PlannedExerciseVO> plannedExerciseVOs) {
        this.startHour = startHour;
        this.presetSeanceId = presetSeanceId;
        this.plannedExercises = plannedExerciseVOs != null ? plannedExerciseVOs : new ArrayList<>();
    }

    public LocalTime getStartHour() {
        return startHour;
    }

    public void setStartHour(LocalTime startHour) {
        this.startHour = startHour;
    }

    public Long getPresetSeanceId() {
        return presetSeanceId;
    }

    public void setPresetSeanceId(Long presetSeanceId) {
        this.presetSeanceId = presetSeanceId;
    }

    public List<PlannedExerciseVO> getPlannedExercises() {
        return plannedExercises;
    }

    public void setPlannedExercises(List<PlannedExerciseVO> plannedExerciseVOs) {
        this.plannedExercises = plannedExerciseVOs != null ? plannedExerciseVOs : new ArrayList<>();
    }
}
