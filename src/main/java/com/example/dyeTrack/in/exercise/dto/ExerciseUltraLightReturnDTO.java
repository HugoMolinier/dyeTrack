package com.example.dyeTrack.in.exercise.dto;

import com.example.dyeTrack.core.entity.Exercise;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExerciseUltraLightReturnDTO {

    private Long idExercise;
    private String nameFR;

    public ExerciseUltraLightReturnDTO() {
    }

    public ExerciseUltraLightReturnDTO(Long idExercise, String nameFR) {
        this.idExercise = idExercise;
        this.nameFR = nameFR;
    }

    public ExerciseUltraLightReturnDTO(Exercise exercise) {
        this.idExercise = exercise.getIdExercise();
        this.nameFR = exercise.getNameFR();
    }

    public Long getIdExercise() {
        return idExercise;
    }

    public void setIdExercise(Long idExercise) {
        this.idExercise = idExercise;
    }

    public String getNameFR() {
        return nameFR;
    }

    public void setNameFR(String nameFR) {
        this.nameFR = nameFR;
    }

}
