package com.example.dyeTrack.in.exercise.dto;

import com.example.dyeTrack.core.entity.Exercise;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExerciceUltraLightReturnDTO implements ExerciceReturnDTO {

    private Long idExercice;
    private String nameFR;

    public ExerciceUltraLightReturnDTO() {
    }

    public ExerciceUltraLightReturnDTO(Long idExercice, String nameFR) {
        this.idExercice = idExercice;
        this.nameFR = nameFR;
    }

    public ExerciceUltraLightReturnDTO(Exercise exercise) {
        this.idExercice = exercise.getIdExercise();
        this.nameFR = exercise.getNameFR();
    }

    public Long getIdExercice() {
        return idExercice;
    }

    public void setIdExercice(Long idExercice) {
        this.idExercice = idExercice;
    }

    public String getNameFR() {
        return nameFR;
    }

    public void setNameFR(String nameFR) {
        this.nameFR = nameFR;
    }

}
