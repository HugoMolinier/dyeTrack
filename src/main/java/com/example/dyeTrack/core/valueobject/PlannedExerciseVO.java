package com.example.dyeTrack.core.valueobject;

import java.util.List;

import jakarta.persistence.Embeddable;

@Embeddable
public class PlannedExerciseVO {

    private int exerciseOrder;
    private Long exerciseId;
    private Long lateraliteId;
    private Long equipmentId;
    private List<SetOfPlannedExerciseVO> sets;

    public PlannedExerciseVO() {
    }

    public PlannedExerciseVO(int exerciseOrder, Long exerciseId, Long lateraliteId,
            Long equipmentId, List<SetOfPlannedExerciseVO> sets) {
        this.exerciseOrder = exerciseOrder;
        this.exerciseId = exerciseId;
        this.lateraliteId = lateraliteId;
        this.equipmentId = equipmentId;
        this.sets = sets;
    }

    // --- Getters & Setters ---
    public int getExerciseOrder() {
        return exerciseOrder;
    }

    public void setExerciseOrder(int exerciseOrder) {
        this.exerciseOrder = exerciseOrder;
    }

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public Long getLateraliteId() {
        return lateraliteId;
    }

    public void setLateraliteId(Long lateraliteId) {
        this.lateraliteId = lateraliteId;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public List<SetOfPlannedExerciseVO> getSets() {
        return sets;
    }

    public void setSets(List<SetOfPlannedExerciseVO> sets) {
        this.sets = sets;
    }
}
