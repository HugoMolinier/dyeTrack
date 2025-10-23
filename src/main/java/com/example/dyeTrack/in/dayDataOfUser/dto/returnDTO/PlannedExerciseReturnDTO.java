package com.example.dyeTrack.in.dayDataOfUser.dto.returnDTO;

import java.util.List;
import java.util.stream.Collectors;

import com.example.dyeTrack.core.entity.PlannedExercise;

public class PlannedExerciseReturnDTO {

    private Long id;
    private int exerciseOrder;

    private Long exerciseId;

    private Long lateraliteId;

    private Long equipmentId;

    private List<SetOfPlannedExerciseReturnDTO> sets;

    public PlannedExerciseReturnDTO() {
    }

    public PlannedExerciseReturnDTO(PlannedExercise plannedExercise) {
        this.id = plannedExercise.getId();
        this.exerciseOrder = plannedExercise.getExerciseOrder();
        this.exerciseId = plannedExercise.getExercise().getIdExercise();
        this.lateraliteId = plannedExercise.getLateralite().getId();
        this.equipmentId = plannedExercise.getEquipment().getId();
        this.sets = plannedExercise.getSetsOfPlannedExercise()
                .stream()
                .map(SetOfPlannedExerciseReturnDTO::new)
                .collect(Collectors.toList());
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<SetOfPlannedExerciseReturnDTO> getSets() {
        return sets;
    }

    public void setSets(List<SetOfPlannedExerciseReturnDTO> setOfPlannedExerciseReturnDTOs) {
        this.sets = setOfPlannedExerciseReturnDTOs;
    }
}
